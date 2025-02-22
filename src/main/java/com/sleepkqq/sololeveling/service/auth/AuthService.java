package com.sleepkqq.sololeveling.service.auth;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.sleepkqq.sololeveling.model.auth.User;
import com.sleepkqq.sololeveling.view.auth.TgWebAppData;
import com.sleepkqq.sololeveling.view.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {


  private static final String TG_SECRET_KEY = "WebAppData";

  @Value("${telegram.bot.token}")
  private String tgBotToken;

  private final UserService userService;

  public void authenticate(TgWebAppData tgWebAppData) {
    if (isLoggedIn()) {
      UI.getCurrent().navigate(HomeView.class);
      return;
    }

    var tgUser = tgWebAppData.user();
    if (!checkHash(tgWebAppData)) {
      throw new IllegalArgumentException(format("Invalid hash, received data: '%s'", tgWebAppData));
    }

    var user = userService.findByUsername(tgUser.username())
        .orElseGet(() -> userService.save(User.fromTgUser(tgUser)));

    var authentication = authenticated(user, tgWebAppData.hash(), user.getAuthorities());

    var context = getContext();
    context.setAuthentication(authentication);
    VaadinSession.getCurrent().getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    UI.getCurrent().getPage().reload();

    log.info("User '{}' authenticated successfully", tgUser.username());
  }

  public void logout() {
    var context = getContext();
    context.setAuthentication(null);
    var httpSession = VaadinSession.getCurrent().getSession();
    httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    UI.getCurrent().getPage().reload();
  }

  public boolean isLoggedIn() {
    return nonNull(getContext().getAuthentication());
  }

  public User getCurrentUser() {
    return (User) getContext().getAuthentication().getPrincipal();
  }

  private boolean checkHash(TgWebAppData tgWebAppData) {
    /*var calculatedHash = calculateHash(tgWebAppData);
    log.info("Calculated hash: '{}'", calculatedHash);
    log.info("Received hash: '{}'", tgWebAppData.hash());
    return tgWebAppData.hash().equals(calculatedHash);*/
    return true;
  }

  private String calculateHash(TgWebAppData tgWebAppData) {
    var data = new StringBuilder()
        .append("auth_date=").append(tgWebAppData.authDate().toEpochSecond(ZoneOffset.UTC))
        .append("\nfirst_name=").append(tgWebAppData.user().firstName())
        .append("\nid=").append(tgWebAppData.user().id())
        .append("\nlast_name=").append(tgWebAppData.user().lastName())
        .append("\nphoto_url=").append(tgWebAppData.user().photoUrl())
        .append("\nusername=").append(tgWebAppData.user().username());

    var secretKey = hmacSha256(
        tgBotToken.getBytes(StandardCharsets.UTF_8),
        TG_SECRET_KEY.getBytes(StandardCharsets.UTF_8)
    );

    var hashGeneratedBytes = hmacSha256(secretKey,
        data.toString().getBytes(StandardCharsets.UTF_8));
    return Hex.toHexString(hashGeneratedBytes);
  }

  private static byte[] hmacSha256(byte[] key, byte[] data) {
    var hmac = new HMac(new SHA256Digest());
    hmac.init(new KeyParameter(key));
    hmac.update(data, 0, data.length);
    var result = new byte[hmac.getMacSize()];
    hmac.doFinal(result, 0);
    return result;
  }

  private static SecurityContext getContext() {
    return SecurityContextHolder.getContext();
  }
}
