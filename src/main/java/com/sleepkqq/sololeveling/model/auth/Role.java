package com.sleepkqq.sololeveling.model.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  USER,
  ADMIN;

  @Override
  public String getAuthority() {
    return name();
  }

  public boolean isUser() {
    return this == USER || isAdmin();
  }

  public boolean isAdmin() {
    return this == ADMIN;
  }
}
