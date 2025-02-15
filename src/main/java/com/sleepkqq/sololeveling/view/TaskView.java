package com.sleepkqq.sololeveling.view;

import com.sleepkqq.sololeveling.view.component.Swiped;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Tasks")
@Route("tasks")
@Menu(order = 1, icon = LineAwesomeIconUrl.TASKS_SOLID)
public class TaskView extends Composite<VerticalLayout> {

  public TaskView() {
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");
    getContent().add(new Swiped(List.of("Строка", "Строка2", "Строка3")));
  }
}
