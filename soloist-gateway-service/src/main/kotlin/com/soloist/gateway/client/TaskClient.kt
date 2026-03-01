package com.soloist.gateway.client

import com.google.protobuf.Empty
import com.soloist.gateway.graphql.types.ActiveTasksResult
import com.soloist.gateway.graphql.types.CompleteTaskResult
import com.soloist.gateway.graphql.types.DailyTasksResult
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.SearchOptionsInput
import com.soloist.gateway.graphql.types.SearchPlayerTasksResult
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.common.SearchEntitiesRequest
import com.soloist.proto.task.*
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TaskClient(
	private val taskStub: TaskServiceGrpc.TaskServiceBlockingStub,
	private val protoMapper: ProtoMapper
) {

	fun getActiveTasks(): ActiveTasksResult {
		val response = taskStub.getActiveTasks(Empty.getDefaultInstance())
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

	fun searchPlayerTasks(
		paging: PagingInput,
		options: SearchOptionsInput?
	): SearchPlayerTasksResult {
		val request = protoMapper.map(paging, options)
		val response = taskStub.searchPlayerTasks(request)
		return protoMapper.map(response)
	}

	fun getDailyTasks(): DailyTasksResult {
		val response = taskStub.getDailyTasks(Empty.getDefaultInstance())
		return protoMapper.map(response)
	}
}
