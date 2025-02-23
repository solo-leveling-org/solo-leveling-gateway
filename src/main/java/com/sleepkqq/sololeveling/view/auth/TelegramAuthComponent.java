package com.sleepkqq.sololeveling.view.auth;

import com.sleepkqq.sololeveling.service.auth.TgAuthService;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.react.ReactAdapterComponent;

@Tag("telegram-auth")
@JsModule("./component/TelegramAuthComponent.js")
public class TelegramAuthComponent extends ReactAdapterComponent {

  private static final String TG_AUTH_DATA = "tgAuthData";

  private final TgAuthService tgAuthService;

  public TelegramAuthComponent(TgAuthService tgAuthService) {
    this.tgAuthService = tgAuthService;
    addAuthListener();
  }

  private void addAuthListener() {
    addStateChangeListener(TG_AUTH_DATA, TgAuthData.class, tgAuthService::authenticate);
  }
}
