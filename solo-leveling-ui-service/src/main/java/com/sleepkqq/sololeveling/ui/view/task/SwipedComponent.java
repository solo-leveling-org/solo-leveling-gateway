package com.sleepkqq.sololeveling.ui.view.task;

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import java.util.List;

@JsModule("https://cdnjs.cloudflare.com/ajax/libs/animejs/3.2.1/anime.min.js")
@CssImport("./styles/swiped.css")
public class SwipedComponent extends Div {

  private final TaskView parentView;

  public SwipedComponent(List<PlayerTaskInfo> playerTasks, TaskView parentView) {
    this.parentView = parentView;
    setClassName("swiped-container");
    playerTasks.forEach(this::createTaskCard);
  }

  private void createTaskCard(PlayerTaskInfo task) {
    Div card = new Div();
    card.setClassName("swiped-item " + task.getTaskInfo().getRarity().name().toLowerCase());

    Div header = new Div(new Span(task.getTaskInfo().getTitle()));
    header.setClassName("task-header");

    Span description = new Span(task.getTaskInfo().getDescription());
    description.setClassName("task-description");

    Div footer = new Div(new Span("Exp: " + task.getTaskInfo().getExperience()));
    footer.setClassName("task-footer");

    card.add(header, description, footer);
    add(card);
    initSwipe(card, task.getTaskInfo().getId());
  }

  private void initSwipe(Div card, String taskId) {
    card.getElement().executeJs("""
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
                        complete: () => $0.$server.completeTask($1)
                    });
                } else if (diff < -100) {
                    anime({
                        targets: card,
                        translateX: -500,
                        rotate: -20,
                        opacity: 0,
                        duration: 500,
                        complete: () => $0.$server.skipTask($1)
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
        """, parentView, taskId);
  }
}