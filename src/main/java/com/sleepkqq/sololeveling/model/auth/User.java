package com.sleepkqq.sololeveling.model.auth;

import com.sleepkqq.sololeveling.dto.auth.TgUser;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
@Document(indexName = "users")
public class User implements UserDetails {

  @Id
  @Field(type = FieldType.Long)
  private final Long id;

  @Field(type = FieldType.Keyword)
  private final String username;

  @Field(type = FieldType.Keyword)
  private final String firstName;

  @Field(type = FieldType.Keyword)
  private final String lastName;

  @Field(type = FieldType.Keyword)
  private final String photoUrl;

  @Enumerated(value = EnumType.STRING)
  @Field(type = FieldType.Keyword)
  private final Locale locale;

  @Enumerated(value = EnumType.STRING)
  @Field(type = FieldType.Keyword)
  private final Set<Role> roles;

  @Override
  public Collection<Role> getAuthorities() {
    return roles;
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
        Set.of(Role.USER)
    );
  }
}
