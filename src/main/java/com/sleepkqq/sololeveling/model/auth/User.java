package com.sleepkqq.sololeveling.model.auth;

import com.sleepkqq.sololeveling.view.auth.TgUser;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Collection;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@Document(indexName = "users")
public class User implements UserDetails {

  @Id
  private String id;

  @Field(type = FieldType.Long)
  private final long telegramId;

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
    return User.builder()
        .telegramId(tgUser.id())
        .username(tgUser.username())
        .firstName(tgUser.firstName())
        .lastName(tgUser.lastName())
        .photoUrl(tgUser.photoUrl())
        .roles(Set.of(Role.USER))
        .build();
  }
}
