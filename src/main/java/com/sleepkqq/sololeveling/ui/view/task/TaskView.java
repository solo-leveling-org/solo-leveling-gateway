package com.sleepkqq.sololeveling.ui.view.task;

import static com.sleepkqq.sololeveling.proto.player.TaskTopic.UNRECOGNIZED;

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo;
import com.sleepkqq.sololeveling.proto.player.TaskTopic;
import com.sleepkqq.sololeveling.ui.api.PlayerApi;
import com.sleepkqq.sololeveling.ui.controller.TestScheduler;
import com.sleepkqq.sololeveling.ui.service.auth.TgAuthService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;
import java.util.List;
import one.util.streamex.StreamEx;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Tasks")
@Route("tasks")
@Menu(order = 2, icon = LineAwesomeIconUrl.TASKS_SOLID)
@PermitAll
public class TaskView extends Composite<VerticalLayout> {

  private final PlayerApi playerApi;
  private final TgAuthService tgAuthService;
  private final TestScheduler testScheduler;
  private Registration broadcasterRegistration;

  public TaskView(
      PlayerApi playerApi,
      TgAuthService tgAuthService,
      TestScheduler testScheduler
  ) {
    this.playerApi = playerApi;
    this.tgAuthService = tgAuthService;
    this.testScheduler = testScheduler;

    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");

    var userId = tgAuthService.getCurrentUser().getId();

    var currentTasks = playerApi.getCurrentTasks(userId);
    if (currentTasks.isEmpty()) {
      showChooseTaskTopics(userId);
    } else {
      showTasks(currentTasks);
    }
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    var userId = tgAuthService.getCurrentUser().getId();
    var ui = attachEvent.getUI();
    broadcasterRegistration = Broadcaster.register(
        userId, () -> ui.access(() -> reloadTasks(userId))
    );
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    broadcasterRegistration.remove();
    broadcasterRegistration = null;
  }

  private void showTasks(List<PlayerTaskInfo> tasks) {
    getContent().add(new SwipedComponent(tasks));
  }

  private void showChooseTaskTopics(long userId) {
    var container = new VerticalLayout();
    container.setWidth("100%");

    var checkboxes = StreamEx.of(TaskTopic.values())
        .remove(UNRECOGNIZED::equals)
        .map(t -> {
          var checkbox = new Checkbox(t.name().replace("_", " ").toLowerCase());
          container.add(checkbox);
          return checkbox;
        })
        .toList();

    var submitButton = new Button("Send", event -> {
      var selectedTopics = StreamEx.of(checkboxes)
          .filter(AbstractField::getValue)
          .map(c -> TaskTopic.valueOf(
              c.getLabel().replace(" ", "_").toUpperCase())
          )
          .toList();

      handleSelectedTopics(userId, selectedTopics);
    });

    container.add(submitButton);
    getContent().add(container);
  }

  private void handleSelectedTopics(long userId, List<TaskTopic> selectedTopics) {
    playerApi.savePlayerTopics(userId, selectedTopics);
    playerApi.generateTasks(userId);
    reloadTasks(userId);
    testScheduler.scheduleBroadcast(userId);
  }

  private void reloadTasks(long userId) {
    getContent().removeAll();
    showTasks(playerApi.getCurrentTasks(userId));
  }
}
