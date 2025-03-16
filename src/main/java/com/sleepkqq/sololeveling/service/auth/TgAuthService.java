package com.sleepkqq.sololeveling.service.auth;

import static com.sleepkqq.sololeveling.localization.LocalizationMessage.ERROR_AUTH_HASH;
import static com.sleepkqq.sololeveling.localization.LocalizationMessage.ERROR_AUTH_REQUIRED;
import static java.util.Objects.nonNull;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.sleepkqq.sololeveling.api.UserApi;
import com.sleepkqq.sololeveling.localization.LocalizationException;
import com.sleepkqq.sololeveling.model.TgAuthData;
import com.sleepkqq.sololeveling.model.UserData;
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

  private final UserApi userApi;
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

    var userData = UserData.fromTgUser(tgWebAppData.user());
    userApi.saveUserInfo(userData);

    var authentication = authenticated(userData, tgWebAppData.hash(), userData.getAuthorities());

    setAuthentication(authentication);
    UI.getCurrent().getPage().reload();

    log.info("User '{}' authenticated successfully", userData.getUsername());
  }

  public void logout() {
    setAuthentication(null);
    UI.getCurrent().getPage().reload();
  }

  public boolean isLoggedIn() {
    return nonNull(getAuthentication());
  }

  public Optional<UserData> findCurrentUser() {
    return Optional.ofNullable(getAuthentication())
        .map(Authentication::getPrincipal)
        .filter(UserData.class::isInstance)
        .map(UserData.class::cast);
  }

  public UserData getCurrentUser() {
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
