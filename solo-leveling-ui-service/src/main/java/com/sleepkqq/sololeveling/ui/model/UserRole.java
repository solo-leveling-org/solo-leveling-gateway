package com.sleepkqq.sololeveling.ui.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
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
