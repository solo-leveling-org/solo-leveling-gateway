package com.sleepkqq.sololeveling.view.task;

import com.sleepkqq.sololeveling.model.task.Rarity;
import com.sleepkqq.sololeveling.model.task.Task;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.List;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Tasks")
@Route("tasks")
@Menu(order = 2, icon = LineAwesomeIconUrl.TASKS_SOLID)
@PermitAll
public class TaskView extends Composite<VerticalLayout> {

  public TaskView() {
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");

    getContent().add(new SwipedComponent(List.of(
        task(Rarity.COMMON),
        task(Rarity.UNCOMMON),
        task(Rarity.RARE),
        task(Rarity.EPIC),
        task(Rarity.LEGENDARY)
    )));
  }

  private Task task(Rarity rarity) {
    return new Task(
        "Таска",
        "Описание",
        30,
        rarity
    );
  }
}
