package com.sleepkqq.sololeveling.ui.view.task;

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;

public class ButtonedTasksComponent extends VerticalLayout {

  public ButtonedTasksComponent(List<PlayerTaskInfo> tasks, TaskView parentView) {
    setWidth("100%");
    tasks.forEach(task -> createTaskCard(task, parentView));
  }

  private void createTaskCard(PlayerTaskInfo task, TaskView parentView) {
    Div card = new Div();
    card.setClassName("task-card " + task.getTaskInfo().getRarity().name().toLowerCase());

    Div header = new Div(new Span(task.getTaskInfo().getTitle()));
    header.setClassName("task-header");

    Span description = new Span(task.getTaskInfo().getDescription());
    description.setClassName("task-description");

    Div footer = new Div();
    footer.setClassName("task-footer");

    Button completeBtn = new Button("✓", e -> {
      animateCardRemoval(card, true);
      parentView.completeTask(task.getTaskInfo().getId());
    });
    completeBtn.addClassName("complete-btn");

    Button skipBtn = new Button("←", e -> {
      animateCardRemoval(card, false);
      parentView.skipTask(task.getTaskInfo().getId());
    });
    skipBtn.addClassName("skip-btn");

    footer.add(completeBtn, skipBtn);
    card.add(header, description, footer);
    add(card);
  }

  private void animateCardRemoval(Div card, boolean isComplete) {
    card.getElement().executeJs("""
            anime({
                targets: this,
                translateX: $0,
                opacity: 0,
                rotate: $1,
                duration: 500,
                complete: () => this.remove()
            });
        """, isComplete ? 500 : -500, isComplete ? 20 : -20);
  }
}