package com.soloist.gateway.resolver

import com.soloist.gateway.client.TaskClient
import com.soloist.gateway.graphql.types.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class TaskResolver(
	private val taskClient: TaskClient
) {

	@SchemaMapping
	fun activeTasks(player: Player): ActiveTasksResult = taskClient.getActiveTasks(player.id)

	@SchemaMapping
	fun dailyTasks(player: Player): DailyTasksResult = taskClient.getDailyTasks(player.id)

	@SchemaMapping
	fun searchPlayerTasks(
		player: Player,
		@Argument paging: PagingInput,
		@Argument options: SearchOptionsInput?
	): ClosedPlayerTasksResult = taskClient.searchClosedPlayerTasks(player.id, paging, options)

	@MutationMapping
	fun generateTasks(): Boolean {
		taskClient.generateTasks()
		return true
	}

	@MutationMapping
	fun completeTask(@Argument id: UUID): CompleteTaskResult = taskClient.completeTask(id)

	@MutationMapping
	fun skipTask(@Argument id: UUID): Boolean {
		taskClient.skipTask(id)
		return true
	}
}
