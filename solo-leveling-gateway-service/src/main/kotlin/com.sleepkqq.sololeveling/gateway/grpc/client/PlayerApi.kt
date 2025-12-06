package com.sleepkqq.sololeveling.gateway.grpc.client

import com.google.protobuf.Empty
import com.sleepkqq.sololeveling.proto.player.*
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlayerApi(
	private val playerStub: PlayerServiceBlockingStub
) {

	fun getActiveTasks(): GetActiveTasksResponse =
		playerStub.getActiveTasks(Empty.newBuilder().build())

	fun getPlayerTopics(): GetPlayerTopicsResponse =
		playerStub.getPlayerTopics(Empty.newBuilder().build())

	fun savePlayerTopics(request: SavePlayerTopicsRequest): Empty =
		playerStub.savePlayerTopics(request)

	fun generateTasks(): Empty =
		playerStub.generateTasks(Empty.newBuilder().build())

	fun completeTask(id: UUID): CompleteTaskResponse =
		playerStub.completeTask(CompleteTaskRequest.newBuilder().setPlayerTaskId(id.toString()).build())

	fun skipTask(id: UUID): Empty =
		playerStub.skipTask(SkipTaskRequest.newBuilder().setPlayerTaskId(id.toString()).build())

	fun searchPlayerTasks(request: SearchEntitiesRequest): SearchPlayerTasksResponse =
		playerStub.searchPlayerTasks(request)

	fun getPlayerBalance(): GetPlayerBalanceResponse =
		playerStub.getPlayerBalance(Empty.newBuilder().build())

	fun searchPlayerBalanceTransactions(request: SearchEntitiesRequest):
			SearchPlayerBalanceTransactionsResponse =
		playerStub.searchPlayerBalanceTransactions(request)
}
