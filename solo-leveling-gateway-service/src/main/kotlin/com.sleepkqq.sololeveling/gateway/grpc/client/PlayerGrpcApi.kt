package com.sleepkqq.sololeveling.gateway.grpc.client

import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service
class PlayerGrpcApi(
	private val playerStub: PlayerServiceBlockingStub
) {

	@Retryable
	fun getActiveTasks(playerId: Long): GetActiveTasksResponse = playerStub.getActiveTasks(
		GetActiveTasksRequest.newBuilder().setPlayerId(playerId).build()
	)

	@Retryable
	fun getPlayerTopics(playerId: Long): GetPlayerTopicsResponse = playerStub.getPlayerTopics(
		GetPlayerTopicsRequest.newBuilder().setPlayerId(playerId).build()
	)

	@Retryable
	fun savePlayerTopics(request: SavePlayerTopicsRequest) {
		playerStub.savePlayerTopics(request)
	}

	@Retryable
	fun generateTasks(playerId: Long) {
		playerStub.generateTasks(GenerateTasksRequest.newBuilder().setPlayerId(playerId).build())
	}

	@Retryable
	fun completeTask(request: CompleteTaskRequest) {
		playerStub.completeTask(request)
	}

	@Retryable
	fun skipTask(request: SkipTaskRequest) {
		playerStub.skipTask(request)
	}
}
