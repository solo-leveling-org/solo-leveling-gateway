package com.sleepkqq.sololeveling.model.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  USER,
  ADMIN;

  @Override
  public String getAuthority() {
    return name();
  }
}
