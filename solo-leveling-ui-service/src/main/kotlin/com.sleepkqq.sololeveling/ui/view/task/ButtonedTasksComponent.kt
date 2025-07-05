package com.sleepkqq.sololeveling.ui.view.task

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import java.util.Locale

class ButtonedTasksComponent(
	tasks: MutableList<PlayerTaskInfo>,
	parentView: TaskView
) : VerticalLayout() {

	init {
		width = "100%"
		tasks.forEach { createTaskCard(it, parentView) }
	}

	private fun createTaskCard(task: PlayerTaskInfo, parentView: TaskView) {
		val card = Div()
		card.className = "task-card " + task.taskInfo.rarity.name.lowercase(Locale.getDefault())

		val header = Div(Span(task.taskInfo.title))
		header.className = "task-header"

		val description = Span(task.taskInfo.description)
		description.className = "task-description"

		val footer = Div()
		footer.className = "task-footer"

		val completeBtn = Button("✓") {
			card.removeFromParent()
			parentView.completeTask(task.taskInfo.id)
		}
		completeBtn.addClassName("complete-btn")

		val skipBtn = Button("←") {
			card.removeFromParent()
			parentView.skipTask(task.taskInfo.id)
		}
		skipBtn.addClassName("skip-btn")

		footer.add(completeBtn, skipBtn)
		card.add(header, description, footer)
		add(card)
	}
}
