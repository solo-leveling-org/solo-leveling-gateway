package com.sleepkqq.sololeveling.gateway.grpc.client

import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import org.springframework.stereotype.Service

@Service
class PlayerGrpcApi(
	private val playerStub: PlayerServiceBlockingStub
) {

	fun getActiveTasks(playerId: Long): GetActiveTasksResponse = playerStub.getActiveTasks(
		GetActiveTasksRequest.newBuilder().setPlayerId(playerId).build()
	)

	fun getPlayerTopics(playerId: Long): GetPlayerTopicsResponse = playerStub.getPlayerTopics(
		GetPlayerTopicsRequest.newBuilder().setPlayerId(playerId).build()
	)

	fun savePlayerTopics(request: SavePlayerTopicsRequest) =
		playerStub.savePlayerTopics(request)

	fun generateTasks(playerId: Long) =
		playerStub.generateTasks(GenerateTasksRequest.newBuilder().setPlayerId(playerId).build())

	fun completeTask(request: CompleteTaskRequest) =
		playerStub.completeTask(request)

	fun skipTask(request: SkipTaskRequest) =
		playerStub.skipTask(request)
}
