package com.sleepkqq.sololeveling.view.auth;

import com.sleepkqq.sololeveling.service.auth.AuthService;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.react.ReactAdapterComponent;

@Tag("telegram-auth")
@JsModule("./component/TelegramAuthComponent.tsx")
public class TelegramAuthComponent extends ReactAdapterComponent {

  private static final String TG_WEB_APP_DATA = "tgWebAppData";

  private final AuthService authService;

  public TelegramAuthComponent(AuthService authService) {
    this.authService = authService;
    addAuthListener();
  }

  private void addAuthListener() {
    addStateChangeListener(TG_WEB_APP_DATA, TgWebAppData.class, authService::authenticate);
  }
}
