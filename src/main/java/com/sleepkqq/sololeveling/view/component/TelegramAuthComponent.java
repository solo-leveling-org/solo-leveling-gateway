package com.sleepkqq.sololeveling.view.component;

import com.sleepkqq.sololeveling.view.auth.TelegramLoggedUser;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;
import com.vaadin.flow.function.SerializableConsumer;

@Tag("telegram-login-button")
@JsModule("./component/TelegramAuthButton.tsx")
@NpmPackage(value = "telegram-react-auth-button", version = "0.4.0")
public class TelegramAuthComponent extends ReactAdapterComponent {

  public void addTelegramLoggedUserListener(SerializableConsumer<TelegramLoggedUser> listener) {
    addStateChangeListener("onAuth", TelegramLoggedUser.class, listener);
  }
}
