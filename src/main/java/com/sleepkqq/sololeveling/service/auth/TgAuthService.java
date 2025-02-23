package com.sleepkqq.sololeveling.service.auth;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.sleepkqq.sololeveling.model.auth.User;
import com.sleepkqq.sololeveling.view.auth.TgAuthData;
import com.sleepkqq.sololeveling.view.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
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
    var tgUser = tgWebAppData.user();
    if (!tgHashService.checkHash(tgAuthData)) {
      throw new IllegalArgumentException(format("Invalid hash, received data: '%s'", tgWebAppData));
    }

    var user = userService.findByUsername(tgUser.username())
        .orElseGet(() -> userService.save(User.fromTgUser(tgUser)));

    var authentication = authenticated(user, tgWebAppData.hash(), user.getAuthorities());

    setAuthentication(authentication);
    UI.getCurrent().getPage().reload();

    log.info("User '{}' authenticated successfully", tgUser.username());
  }

  public void logout() {
    setAuthentication(null);
    UI.getCurrent().getPage().reload();
  }

  public boolean isLoggedIn() {
    return nonNull(getAuthentication());
  }

  public User getCurrentUser() {
    return (User) getAuthentication().getPrincipal();
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
