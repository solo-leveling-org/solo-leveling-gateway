package com.sleepkqq.sololeveling.view.task;

import com.sleepkqq.sololeveling.entity.task.Rarity;
import com.sleepkqq.sololeveling.entity.task.Task;
import com.sleepkqq.sololeveling.view.component.SwipedComponent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Tasks")
@Route("tasks")
@Menu(order = 2, icon = LineAwesomeIconUrl.TASKS_SOLID)
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
    return Task.builder()
        .title("Таска")
        .description("Описание")
        .experience(30)
        .rarity(rarity)
        .build();
  }
}
