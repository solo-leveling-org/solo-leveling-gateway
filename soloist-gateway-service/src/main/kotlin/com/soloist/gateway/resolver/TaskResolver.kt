package com.soloist.gateway.resolver

import com.soloist.gateway.client.TaskClient
import com.soloist.gateway.graphql.types.ActiveTasksResult
import com.soloist.gateway.graphql.types.CompleteTaskResult
import com.soloist.gateway.graphql.types.DailyTasksResult
import com.soloist.gateway.graphql.types.PagingInput
import com.soloist.gateway.graphql.types.SearchOptionsInput
import com.soloist.gateway.graphql.types.SearchPlayerTasksResult
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class TaskResolver(
	private val taskClient: TaskClient
) {

	@QueryMapping
	fun activeTasks(): ActiveTasksResult = taskClient.getActiveTasks()

	@QueryMapping
	fun dailyTasks(): DailyTasksResult = taskClient.getDailyTasks()

	@QueryMapping
	fun searchPlayerTasks(
		@Argument paging: PagingInput,
		@Argument options: SearchOptionsInput?
	): SearchPlayerTasksResult = taskClient.searchPlayerTasks(paging, options)

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
