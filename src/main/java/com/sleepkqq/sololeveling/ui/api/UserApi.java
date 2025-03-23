package com.sleepkqq.sololeveling.ui.api;

import com.sleepkqq.sololeveling.ui.mapper.DtoMapper;
import com.sleepkqq.sololeveling.ui.model.UserData;
import com.sleepkqq.sololeveling.proto.user.AuthUserRequest;
import com.sleepkqq.sololeveling.proto.user.GetUserInfoRequest;
import com.sleepkqq.sololeveling.proto.user.UserInfo;
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// todo: add retry
public class UserApi {

  private final UserServiceBlockingStub userStub;
  private final DtoMapper dtoMapper;

  public UserInfo getUserInfo(long userId) {
    var response = userStub.getUserInfo(GetUserInfoRequest.newBuilder()
        .setUserId(userId)
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to get UserInfo, userId=" + userId);
    }

    return response.getUserInfo();
  }

  public void authUser(UserData userData) {
    var response = userStub.authUser(AuthUserRequest.newBuilder()
        .setUserInfo(dtoMapper.map(userData))
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException(
          "Failed to save UserInfo, userId=" + userData.getUsername()
      );
    }
  }
}
