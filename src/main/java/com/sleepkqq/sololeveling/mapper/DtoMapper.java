package com.sleepkqq.sololeveling.mapper;

import com.sleepkqq.sololeveling.model.UserData;
import com.sleepkqq.sololeveling.proto.user.UserInfo;
import com.sleepkqq.sololeveling.proto.user.UserRole;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

  public <T, R> List<R> mapCollection(Collection<T> collection, Function<T, R> mapper) {
    return StreamEx.of(collection).map(mapper).toList();
  }

  public UserInfo map(UserData user) {
    return UserInfo.newBuilder()
        .setId(user.getId())
        .setUsername(user.getUsername())
        .setFirstName(user.getFirstName())
        .setLastName(user.getLastName())
        .setPhotoUrl(user.getPhotoUrl())
        .setLocale(user.getLocale().toLanguageTag())
        .addAllRole(mapCollection(user.getRoles(), this::map))
        .build();
  }

  public UserData map(UserInfo userInfo) {
    return new UserData(
        userInfo.getId(),
        userInfo.getUsername(),
        userInfo.getFirstName(),
        userInfo.getLastName(),
        userInfo.getPhotoUrl(),
        Locale.forLanguageTag(userInfo.getLocale()),
        mapCollection(userInfo.getRoleList(), this::map)
    );
  }

  public UserRole map(com.sleepkqq.sololeveling.model.UserRole userRole) {
    return UserRole.valueOf(userRole.name());
  }

  public com.sleepkqq.sololeveling.model.UserRole map(UserRole userRole) {
    return com.sleepkqq.sololeveling.model.UserRole.valueOf(userRole.name());
  }
}
