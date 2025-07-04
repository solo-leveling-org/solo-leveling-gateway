package com.sleepkqq.sololeveling.ui.view.task

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import java.util.Locale

@JsModule("https://cdnjs.cloudflare.com/ajax/libs/animejs/3.2.1/anime.min.js")
@CssImport("./styles/swiped.css")
class SwipedComponent(
	playerTasks: MutableList<PlayerTaskInfo>,
	private val parentView: TaskView
) : Div() {

	init {
		className = "swiped-container"
		playerTasks.forEach { createTaskCard(it) }
	}

	private fun createTaskCard(task: PlayerTaskInfo) {
		val card = Div()
		card.className = "swiped-item " + task.taskInfo.rarity.name.lowercase(Locale.getDefault())

		val header = Div(Span(task.taskInfo.title))
		header.className = "task-header"

		val description = Span(task.taskInfo.description)
		description.className = "task-description"

		val footer = Div(Span("Exp: " + task.taskInfo.experience))
		footer.className = "task-footer"

		card.add(header, description, footer)
		add(card)
		initSwipe(card, task.taskInfo.id)
	}

	private fun initSwipe(card: Div, taskId: String) {
		card.element.executeJs(
			"""
            const card = this;
            let startX;

            card.addEventListener('touchstart', (e) => {
                startX = e.touches[0].clientX;
                anime({ targets: card, scale: 0.98, duration: 100 });
            });

            card.addEventListener('touchmove', (e) => {
                const diff = e.touches[0].clientX - startX;
                anime({
                    targets: card,
                    translateX: diff,
                    rotate: diff * 0.1,
                    opacity: 1 - Math.abs(diff) / 300,
                    duration: 0
                });
            });

            card.addEventListener('touchend', (e) => {
                const diff = e.changedTouches[0].clientX - startX;

                if (diff > 100) {
                    anime({
                        targets: card,
                        translateX: 500,
                        rotate: 20,
                        opacity: 0,
                        duration: 500,
                        complete: () => ${'$'}0.${'$'}server.completeTask(${'$'}1)
                    });
                } else if (diff < -100) {
                    anime({
                        targets: card,
                        translateX: -500,
                        rotate: -20,
                        opacity: 0,
                        duration: 500,
                        complete: () => ${'$'}0.${'$'}server.skipTask(${'$'}1)
                    });
                } else {
                    anime({
                        targets: card,
                        translateX: 0,
                        rotate: 0,
                        opacity: 1,
                        scale: 1,
                        duration: 300
                    });
                }
            });
        
        """.trimIndent(),
			parentView,
			taskId
		)
	}
}
