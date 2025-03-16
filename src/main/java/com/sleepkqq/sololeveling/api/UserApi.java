package com.sleepkqq.sololeveling.api;

import com.sleepkqq.sololeveling.mapper.DtoMapper;
import com.sleepkqq.sololeveling.model.UserData;
import com.sleepkqq.sololeveling.proto.user.GetUserInfoRequest;
import com.sleepkqq.sololeveling.proto.user.GetUserTasksRequest;
import com.sleepkqq.sololeveling.proto.user.SaveUserRequest;
import com.sleepkqq.sololeveling.proto.user.UserInfo;
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub;
import com.sleepkqq.sololeveling.proto.user.UserTasks;
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
        .setId(userId)
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to get UserInfo, userId=" + userId);
    }

    return response.getUserInfo();
  }

  public void saveUserInfo(UserData userData) {
    var response = userStub.saveUser(SaveUserRequest.newBuilder()
        .setUserInfo(dtoMapper.map(userData))
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException(
          "Failed to save UserInfo, userId=" + userData.getUsername()
      );
    }
  }

  public UserTasks getUserTasks(long userId) {
    var response = userStub.getUserTasks(GetUserTasksRequest.newBuilder()
        .setId(userId)
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to get UserTasks, userId=" + userId);
    }

    return response.getUserTasks();
  }
}
