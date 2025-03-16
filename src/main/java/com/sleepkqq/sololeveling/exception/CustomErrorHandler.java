package com.sleepkqq.sololeveling.exception;

import static com.sleepkqq.sololeveling.localization.LocalizationMessage.ERROR_UNEXPECTED;

import com.sleepkqq.sololeveling.localization.LocalizationException;
import com.sleepkqq.sololeveling.model.UserData;
import com.sleepkqq.sololeveling.service.auth.TgAuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public class CustomErrorHandler implements ErrorHandler {

  private final TgAuthService tgAuthService;
  private final MessageSource messageSource;

  @Override
  public void error(ErrorEvent errorEvent) {
    var locale = tgAuthService.findCurrentUser().map(UserData::getLocale).orElse(Locale.ENGLISH);
    Optional.ofNullable(UI.getCurrent())
        .ifPresent(c -> c.access(() -> Notification.show(messageSource.getMessage(
            errorEvent.getThrowable() instanceof LocalizationException l
                ? l.getLocalizationMessage().getPath()
                : ERROR_UNEXPECTED.getPath(),
            null,
            locale
        ))));
  }
}
