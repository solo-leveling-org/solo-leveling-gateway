package com.soloist.gateway.client

import com.soloist.gateway.graphql.types.CreateCustomTaskResult
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.TaskHistoryResult
import com.soloist.gateway.graphql.types.TasksResult
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.proto.task.CreateCustomTaskRequest
import com.soloist.proto.task.GetTaskHistoryRequest
import com.soloist.proto.task.GetTasksRequest
import com.soloist.proto.task.TaskServiceGrpcKt
import org.springframework.stereotype.Service

@Service
class TaskClient(
	private val taskStub: TaskServiceGrpcKt.TaskServiceCoroutineStub,
	private val protoMapper: ProtoMapper
) {

	suspend fun getTasks(playerId: Long): TasksResult {
		val request = GetTasksRequest.newBuilder().setPlayerId(playerId).build()
		val response = taskStub.getTasks(request)
		return protoMapper.map(response)
	}

	suspend fun createCustomTask(name: String): CreateCustomTaskResult {
		val request = CreateCustomTaskRequest.newBuilder()
			.setName(name)
			.build()
		val response = taskStub.createCustomTask(request)
		return protoMapper.map(response)
	}

	suspend fun getTaskHistory(playerId: Long, paging: PagingInput): TaskHistoryResult {
		val request = GetTaskHistoryRequest.newBuilder()
			.setPlayerId(playerId)
			.setPaging(protoMapper.map(paging))
			.build()
		val response = taskStub.getTaskHistory(request)
		return protoMapper.map(response)
	}
}
