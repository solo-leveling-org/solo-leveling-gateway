package com.sleepkqq.sololeveling.service.auth;

import static com.sleepkqq.sololeveling.localization.LocalizationMessage.ERROR_AUTH_HASH;
import static com.sleepkqq.sololeveling.localization.LocalizationMessage.ERROR_AUTH_REQUIRED;
import static java.util.Objects.nonNull;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.sleepkqq.sololeveling.localization.LocalizationException;
import com.sleepkqq.sololeveling.model.auth.User;
import com.sleepkqq.sololeveling.view.auth.TgAuthData;
import com.sleepkqq.sololeveling.view.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TgAuthService {

  private final UserService userService;
  private final TgHashService tgHashService;

  public void authenticate(TgAuthData tgAuthData) {
    if (isLoggedIn()) {
      UI.getCurrent().navigate(HomeView.class);
      return;
    }

    var tgWebAppData = tgAuthData.tgWebAppData();
    if (!tgHashService.checkHash(tgAuthData.initData(), tgWebAppData.hash())) {
      log.error("Invalid hash, received data: '{}'", tgWebAppData);
      throw new LocalizationException(ERROR_AUTH_HASH);
    }

    var user = userService.save(User.fromTgUser(tgWebAppData.user()));

    var authentication = authenticated(user, tgWebAppData.hash(), user.getAuthorities());

    setAuthentication(authentication);
    UI.getCurrent().getPage().reload();

    log.info("User '{}' authenticated successfully", user.getUsername());
  }

  public void logout() {
    setAuthentication(null);
    UI.getCurrent().getPage().reload();
  }

  public boolean isLoggedIn() {
    return nonNull(getAuthentication());
  }

  public Optional<User> findCurrentUser() {
    return Optional.ofNullable(getAuthentication())
        .map(Authentication::getPrincipal)
        .filter(User.class::isInstance)
        .map(User.class::cast);
  }

  public User getCurrentUser() {
    return findCurrentUser().orElseThrow(() -> new LocalizationException(ERROR_AUTH_REQUIRED));
  }

  private static SecurityContext getContext() {
    return SecurityContextHolder.getContext();
  }

  private static Authentication getAuthentication() {
    return getContext().getAuthentication();
  }

  private static void setAuthentication(Authentication authentication) {
    var context = getContext();
    context.setAuthentication(authentication);
    VaadinSession.getCurrent().getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
  }
}
