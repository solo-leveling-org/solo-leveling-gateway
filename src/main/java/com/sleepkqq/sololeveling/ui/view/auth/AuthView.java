package com.sleepkqq.sololeveling.ui.view.auth;

import com.sleepkqq.sololeveling.ui.exception.CustomErrorHandler;
import com.sleepkqq.sololeveling.ui.service.auth.TgAuthService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.context.MessageSource;

@PageTitle("Authentication")
@Route("auth")
@Uses(Icon.class)
@AnonymousAllowed
public class AuthView extends Composite<VerticalLayout> {

  public AuthView(TgAuthService tgAuthService, MessageSource messageSource) {
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");
    getContent().setJustifyContentMode(JustifyContentMode.CENTER);
    getContent().setAlignItems(Alignment.CENTER);

    var customErrorHandler = new CustomErrorHandler(tgAuthService, messageSource);
    VaadinSession.getCurrent().setErrorHandler(customErrorHandler);

    var telegramAuthComponent = new TelegramAuthComponent(tgAuthService);
    getContent().add(telegramAuthComponent);
  }
}
