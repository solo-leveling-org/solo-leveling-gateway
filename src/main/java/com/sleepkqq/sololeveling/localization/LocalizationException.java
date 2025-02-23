package com.sleepkqq.sololeveling.localization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LocalizationException extends RuntimeException {

  private final LocalizationMessage localizationMessage;
}
