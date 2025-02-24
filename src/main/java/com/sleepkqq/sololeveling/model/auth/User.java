package com.sleepkqq.sololeveling.model.auth;

import com.sleepkqq.sololeveling.dto.auth.TgUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  private Long id;

  private String username;

  private String firstName;

  private String lastName;

  private String photoUrl;

  private Locale locale;

  @Enumerated(value = EnumType.STRING)
  private Role role;

  @Override
  public Collection<Role> getAuthorities() {
    return Set.of(role);
  }

  @Override
  public String getPassword() {
    return null;
  }

  public static User fromTgUser(TgUser tgUser) {
    return new User(
        tgUser.id(),
        tgUser.username(),
        tgUser.firstName(),
        tgUser.lastName(),
        tgUser.photoUrl(),
        Optional.of(tgUser.languageCode())
            .filter("ru"::equalsIgnoreCase)
            .map(Locale::forLanguageTag)
            .orElse(Locale.ENGLISH),
        Role.USER
    );
  }
}
