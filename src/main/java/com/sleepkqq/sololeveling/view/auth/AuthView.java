package com.sleepkqq.sololeveling.view.auth;

import com.sleepkqq.sololeveling.service.auth.TgAuthService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Authentication")
@Route("auth")
@Uses(Icon.class)
@AnonymousAllowed
public class AuthView extends Composite<VerticalLayout> {

  public AuthView(TgAuthService tgAuthService) {
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");
    getContent().setJustifyContentMode(JustifyContentMode.CENTER);
    getContent().setAlignItems(Alignment.CENTER);

    var telegramAuthComponent = new TelegramAuthComponent(tgAuthService);
    getContent().add(telegramAuthComponent);
  }
}
