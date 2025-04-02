package com.sleepkqq.sololeveling.ui.api;

import com.sleepkqq.sololeveling.proto.player.GenerateTasksRequest;
import com.sleepkqq.sololeveling.proto.player.GetCurrentTasksRequest;
import com.sleepkqq.sololeveling.proto.player.GetPlayerInfoRequest;
import com.sleepkqq.sololeveling.proto.player.PlayerInfo;
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub;
import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo;
import com.sleepkqq.sololeveling.proto.player.SavePlayerTopicsRequest;
import com.sleepkqq.sololeveling.proto.player.TaskTopic;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// todo: add retry
public class PlayerApi {

  private final PlayerServiceBlockingStub playerStub;

  public PlayerInfo getPlayerInfo(long playerId) {
    var response = playerStub.getPlayerInfo(GetPlayerInfoRequest.newBuilder()
        .setPlayerId(playerId)
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to get player info, playerId=" + playerId);
    }

    return response.getPlayerInfo();
  }

  public List<PlayerTaskInfo> getCurrentTasks(long playerId) {
    var response = playerStub.getCurrentTasks(GetCurrentTasksRequest.newBuilder()
        .setPlayerId(playerId)
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to get current tasks, playerId=" + playerId);
    }

    return response.getCurrentTaskList();
  }

  public void savePlayerTopics(long playerId, List<TaskTopic> topics) {
    var response = playerStub.savePlayerTopics(SavePlayerTopicsRequest.newBuilder()
        .setPlayerId(playerId)
        .addAllTopic(topics)
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to save player topics, playerId=" + playerId);
    }
  }

  public void generateTasks(long playerId) {
    var response = playerStub.generateTasks(GenerateTasksRequest.newBuilder()
        .setPlayerId(playerId)
        .build()
    );

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to generate tasks, playerId=" + playerId);
    }
  }
}
