package com.soloist.gateway.resolver

import com.soloist.gateway.client.TaskClient
import com.soloist.gateway.graphql.types.CreateCustomTaskResult
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.Player
import com.soloist.gateway.graphql.types.TaskHistoryResult
import com.soloist.gateway.graphql.types.TasksResult
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class TaskResolver(
	private val taskClient: TaskClient
) {

	@SchemaMapping
	suspend fun tasks(player: Player): TasksResult = taskClient.getTasks(player.id)

	@SchemaMapping
	suspend fun taskHistory(
		player: Player,
		@Argument paging: PagingInput
	): TaskHistoryResult = taskClient.getTaskHistory(player.id, paging)

	@MutationMapping
	suspend fun createCustomTask(
		@Argument name: String
	): CreateCustomTaskResult = taskClient.createCustomTask(name)
}
