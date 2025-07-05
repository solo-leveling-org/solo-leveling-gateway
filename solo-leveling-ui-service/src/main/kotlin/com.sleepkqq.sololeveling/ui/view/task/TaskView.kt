package com.sleepkqq.sololeveling.ui.view.task

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo
import com.sleepkqq.sololeveling.proto.player.TaskTopic
import com.sleepkqq.sololeveling.ui.api.PlayerApi
import com.sleepkqq.sololeveling.ui.broadcast.TaskUpdatesTopic
import com.sleepkqq.sololeveling.ui.mapper.DtoMapper
import com.sleepkqq.sololeveling.ui.service.TgAuthService
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.shared.Registration
import jakarta.annotation.security.PermitAll
import org.vaadin.lineawesome.LineAwesomeIconUrl
import java.util.Locale

@PageTitle("Tasks")
@Route("tasks")
@Menu(order = 1.0, icon = LineAwesomeIconUrl.TASKS_SOLID)
@PermitAll
class TaskView(
	private val playerApi: PlayerApi,
	private val tgAuthService: TgAuthService,
	private val taskUpdatesTopic: TaskUpdatesTopic,
	private val dtoMapper: DtoMapper
) : Composite<VerticalLayout>() {

	private var broadcasterRegistration: Registration? = null
	private var currentTasks: MutableList<PlayerTaskInfo>

	init {
		content.width = "100%"
		content.style.set("flex-grow", "1")

		val userId = tgAuthService.currentUser.id
		this.currentTasks = playerApi.getCurrentTasks(userId).toMutableList()

		if (currentTasks.isEmpty()) {
			showChooseTaskTopics(userId)
		} else {
			showTasks(currentTasks)
		}
	}

	override fun onAttach(attachEvent: AttachEvent) {
		val userId = tgAuthService.currentUser.id
		broadcasterRegistration = taskUpdatesTopic.subscribe(userId)
		{ attachEvent.ui.access { reloadTasks(userId, it.message) } }
	}

	override fun onDetach(detachEvent: DetachEvent) {
		broadcasterRegistration?.remove()
		broadcasterRegistration = null
	}

	private fun showTasks(tasks: MutableList<PlayerTaskInfo>) {
		content.removeAll()
		this.currentTasks = tasks
		refreshTasksDisplay()
	}

	private fun refreshTasksDisplay() {
		content.children
			.filter { it !is Button }
			.forEach { content.remove(it) }

		content.add(ButtonedTasksComponent(currentTasks, this))
	}

	fun completeTask(playerTaskId: String) {
		handleTaskCompletion(playerTaskId, true)
	}

	fun skipTask(playerTaskId: String) {
		handleTaskCompletion(playerTaskId, false)
	}

	private fun handleTaskCompletion(playerTaskId: String, completed: Boolean) {
		currentTasks.removeIf { it.taskInfo.id == playerTaskId }
		val id = dtoMapper.map(playerTaskId)
		if (completed) {
			playerApi.completeTask(id)
		} else {
			playerApi.skipTask(id)
		}

		Notification.show(if (completed) "Задача завершена!" else "Задача пропущена")
	}

	private fun showChooseTaskTopics(userId: Long) {
		val container = VerticalLayout()
		container.width = "100%"

		val checkboxes = TaskTopic.entries
			.filter { TaskTopic.UNRECOGNIZED != it }
			.map { TaskTopicMapper.to(it) }
			.onEach { container.add(it) }

		val submitButton = Button("Send") {
			handleSelectedTopics(
				userId,
				checkboxes
					.filter { it.value }
					.map { TaskTopicMapper.from(it) }
			)
		}

		container.add(submitButton)
		content.add(container)
	}

	private fun handleSelectedTopics(userId: Long, selectedTopics: List<TaskTopic>) {
		playerApi.savePlayerTopics(userId, selectedTopics)
		playerApi.generateTasks(userId)
		reloadTasks(userId, "Your tasks have started to be generated!")
	}

	private fun reloadTasks(userId: Long, message: String) {
		currentTasks = playerApi.getCurrentTasks(userId).toMutableList()
		showTasks(currentTasks)
		Notification.show(message)
	}

	private object TaskTopicMapper {

		private const val SPACE = " "
		private const val UNDERSCORE = "_"

		fun from(checkbox: Checkbox): TaskTopic {
			return TaskTopic.valueOf(
				checkbox.label.replace(SPACE, UNDERSCORE).uppercase(Locale.getDefault())
			)
		}

		fun to(topic: TaskTopic): Checkbox {
			return Checkbox(topic.name.replace(UNDERSCORE, SPACE).lowercase(Locale.getDefault()))
		}
	}
}
