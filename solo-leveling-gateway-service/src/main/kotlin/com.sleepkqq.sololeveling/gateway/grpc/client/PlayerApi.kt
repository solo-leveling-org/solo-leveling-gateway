package com.sleepkqq.sololeveling.gateway.grpc.client

import com.google.protobuf.Empty
import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import org.springframework.stereotype.Service

@Service
class PlayerApi(
	private val playerStub: PlayerServiceBlockingStub
) {

	fun getActiveTasks(playerId: Long): GetActiveTasksResponse = playerStub.getActiveTasks(
		GetActiveTasksRequest.newBuilder().setPlayerId(playerId).build()
	)

	fun getPlayerTopics(playerId: Long): GetPlayerTopicsResponse = playerStub.getPlayerTopics(
		GetPlayerTopicsRequest.newBuilder().setPlayerId(playerId).build()
	)

	fun savePlayerTopics(request: SavePlayerTopicsRequest): Empty =
		playerStub.savePlayerTopics(request)

	fun generateTasks(playerId: Long): Empty =
		playerStub.generateTasks(GenerateTasksRequest.newBuilder().setPlayerId(playerId).build())

	fun completeTask(request: CompleteTaskRequest): CompleteTaskResponse =
		playerStub.completeTask(request)

	fun skipTask(request: SkipTaskRequest): Empty = playerStub.skipTask(request)

	fun searchPlayerTasks(request: SearchPlayerTasksRequest): SearchPlayerTasksResponse =
		playerStub.searchPlayerTasks(request)

	fun getPlayerBalance(playerId: Long): GetPlayerBalanceResponse =
		playerStub.getPlayerBalance(GetPlayerBalanceRequest.newBuilder().setPlayerId(playerId).build())

	fun searchPlayerBalanceTransactions(request: SearchPlayerBalanceTransactionsRequest):
			SearchPlayerBalanceTransactionsResponse =
		playerStub.searchPlayerBalanceTransactions(request)
}
