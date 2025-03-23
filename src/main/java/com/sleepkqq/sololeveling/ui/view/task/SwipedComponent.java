package com.sleepkqq.sololeveling.ui.view.task;

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo;
import com.sleepkqq.sololeveling.proto.player.TaskRarity;
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

  public SwipedComponent(List<PlayerTaskInfo> playerTasks) {
    setClassName("swiped-container");
    playerTasks.forEach(playerTask -> {
      var taskInfo = playerTask.getTaskInfo();

      var listItem = new Div();
      listItem.setClassName("swiped-item");

      var header = new Div();
      header.setClassName("task-header");

      resolveTitle(header, taskInfo.getTitle());

      listItem.add(header);

      resolveDescription(listItem, taskInfo.getDescription());

      var footer = new Div();
      footer.setClassName("task-footer");

      resolveExperience(footer, taskInfo.getExperience());
      listItem.add(footer);

      resolveRarity(listItem, taskInfo.getRarity());

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
