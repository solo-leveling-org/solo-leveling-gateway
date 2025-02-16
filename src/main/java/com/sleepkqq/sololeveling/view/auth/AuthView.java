package com.sleepkqq.sololeveling.view.auth;

import com.sleepkqq.sololeveling.view.component.TelegramAuthComponent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Slf4j
@PageTitle("Authentication")
@Route("auth")
@Menu(order = 1, icon = LineAwesomeIconUrl.SIGN_IN_ALT_SOLID)
@Uses(Icon.class)
public class AuthView extends Composite<VerticalLayout> {

  public AuthView() {
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");

    var telegramAuthComponent = new TelegramAuthComponent();
    telegramAuthComponent.addTelegramLoggedUserListener(
        user -> log.info(user.toString())
    );
    getContent().add(telegramAuthComponent);
  }
}
