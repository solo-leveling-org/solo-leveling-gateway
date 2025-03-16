package com.sleepkqq.sololeveling.view.task;

import com.sleepkqq.sololeveling.proto.task.Task;
import com.sleepkqq.sololeveling.proto.task.TaskRarity;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import java.util.List;

@Tag("swiped-container")
@JsModule("swiped")
@CssImport(value = "./styles/swiped.css", themeFor = "swiped-container")
public class SwipedComponent extends Div {

  public SwipedComponent(List<Task> tasks) {
    setClassName("swiped-container");
    tasks.forEach(task -> {
      var listItem = new Div();
      listItem.setClassName("swiped-item");

      var header = new Div();
      header.setClassName("task-header");

      resolveTitle(header, task.getTitle());

      listItem.add(header);

      resolveDescription(listItem, task.getDescription());

      var footer = new Div();
      footer.setClassName("task-footer");

      resolveExperience(footer, task.getExperience());
      listItem.add(footer);

      resolveRarity(listItem, task.getRarity());

      add(listItem);
    });

    getElement().executeJs(
        "Swiped.init({ query: '.swiped-item', list: true, left: 200, right: 200 });"
    );
  }

  private void resolveTitle(Div header, String title) {
    var titleSpan = new Span(title);
    titleSpan.setClassName("task-title");
    header.add(titleSpan);
  }

  private void resolveDescription(Div listItem, String description) {
    var descriptionSpan = new Span(description);
    descriptionSpan.setClassName("task-description");
    listItem.add(descriptionSpan);
  }

  private void resolveExperience(Div footer, int experience) {
    var experienceSpan = new Span("Exp: " + experience);
    experienceSpan.setClassName("task-experience");
    footer.add(experienceSpan);
  }

  private void resolveRarity(Div listItem, TaskRarity rarity) {
    listItem.addClassName(rarity.name().toLowerCase());
  }
}
