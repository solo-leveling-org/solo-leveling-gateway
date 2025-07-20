package com.sleepkqq.sololeveling.gateway.api.grpc

import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import org.springframework.stereotype.Service
import java.util.UUID

// todo: add retry
@Service
class PlayerApi(
	private val playerStub: PlayerServiceBlockingStub
) {

	fun getPlayerInfo(playerId: Long): PlayerInfo = playerStub.getPlayerInfo(
		GetPlayerInfoRequest.newBuilder()
			.setPlayerId(playerId)
			.build()
	)
		.playerInfo

	fun getCurrentTasks(playerId: Long): List<PlayerTaskInfo> = playerStub.getCurrentTasks(
		GetCurrentTasksRequest.newBuilder()
			.setPlayerId(playerId)
			.build()
	)
		.currentTaskList

	fun savePlayerTopics(playerId: Long, topics: List<TaskTopic>) = playerStub.savePlayerTopics(
		SavePlayerTopicsRequest.newBuilder()
			.setPlayerId(playerId)
			.addAllTopic(topics)
			.build()
	)

	fun generateTasks(playerId: Long) = playerStub.generateTasks(
		GenerateTasksRequest.newBuilder()
			.setPlayerId(playerId)
			.build()
	)

	fun completeTask(playerTaskId: UUID) = playerStub.completeTask(
		CompleteTaskRequest.newBuilder()
			.setPlayerTaskId(playerTaskId.toString())
			.build()
	)

	fun skipTask(playerTaskId: UUID) = playerStub.skipTask(
		SkipTaskRequest.newBuilder()
			.setPlayerTaskId(playerTaskId.toString())
			.build()
	)
}
