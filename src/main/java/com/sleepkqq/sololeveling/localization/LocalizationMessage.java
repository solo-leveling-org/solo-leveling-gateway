package com.sleepkqq.sololeveling.localization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocalizationMessage {
  ERROR_UNEXPECTED("error.unexpected"),
  ERROR_AUTH_HASH("error.auth.hash"),
  ERROR_AUTH_REQUIRED("error.auth.required");

  private final String path;
}
