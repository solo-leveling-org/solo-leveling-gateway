package com.soloist.gateway.client

import com.google.protobuf.Empty
import com.soloist.gateway.graphql.types.ActiveTasksResult
import com.soloist.gateway.graphql.types.ClosedPlayerTasksResult
import com.soloist.gateway.graphql.types.CompleteTaskResult
import com.soloist.gateway.graphql.types.DailyTasksResult
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.SearchOptionsInput
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.task.*
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TaskClient(
	private val taskStub: TaskServiceGrpc.TaskServiceBlockingStub,
	private val protoMapper: ProtoMapper
) {

	fun getActiveTasks(playerId: Long): ActiveTasksResult {
		val request = GetActiveTasksRequest.newBuilder().setPlayerId(playerId).build()
		val response = taskStub.getActiveTasks(request)
		return protoMapper.map(response)
	}

	fun generateTasks() {
		taskStub.generateTasks(Empty.getDefaultInstance())
	}

	fun completeTask(id: UUID): CompleteTaskResult {
		val request = CompleteTaskRequest.newBuilder().setPlayerTaskId(id.toString()).build()
		val response = taskStub.completeTask(request)
		return protoMapper.map(response)
	}

	fun skipTask(id: UUID) {
		val request = SkipTaskRequest.newBuilder().setPlayerTaskId(id.toString()).build()
		taskStub.skipTask(request)
	}

	fun searchClosedPlayerTasks(
		playerId: Long,
		paging: PagingInput,
		options: SearchOptionsInput?
	): ClosedPlayerTasksResult {
		val request = protoMapper.mapTasks(playerId, paging, options)
		val response = taskStub.searchClosedTasks(request)
		return protoMapper.map(response)
	}

	fun getDailyTasks(playerId: Long): DailyTasksResult {
		val request = GetDailyTasksRequest.newBuilder().setPlayerId(playerId).build()
		val response = taskStub.getDailyTasks(request)
		return protoMapper.map(response)
	}
}
