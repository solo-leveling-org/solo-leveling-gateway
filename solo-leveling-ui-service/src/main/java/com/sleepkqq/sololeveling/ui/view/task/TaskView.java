package com.sleepkqq.sololeveling.ui.view.task;

import static com.sleepkqq.sololeveling.proto.player.TaskTopic.UNRECOGNIZED;

import com.sleepkqq.sololeveling.proto.player.PlayerTaskInfo;
import com.sleepkqq.sololeveling.proto.player.TaskTopic;
import com.sleepkqq.sololeveling.ui.api.PlayerApi;
import com.sleepkqq.sololeveling.ui.broadcast.TaskUpdatesTopic;
import com.sleepkqq.sololeveling.ui.service.TgAuthService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;
import one.util.streamex.StreamEx;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Tasks")
@Route("tasks")
@Menu(order = 1, icon = LineAwesomeIconUrl.TASKS_SOLID)
@PermitAll
public class TaskView extends Composite<VerticalLayout> {

  private final PlayerApi playerApi;
  private final TgAuthService tgAuthService;
  private final TaskUpdatesTopic taskUpdatesTopic;
  private Registration broadcasterRegistration;
  private List<PlayerTaskInfo> currentTasks;
  private boolean isSwipeMode = true; // Флаг текущего режима

  public TaskView(PlayerApi playerApi, TgAuthService tgAuthService, TaskUpdatesTopic taskUpdatesTopic) {
    this.playerApi = playerApi;
    this.tgAuthService = tgAuthService;
    this.taskUpdatesTopic = taskUpdatesTopic;

    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");

    var userId = tgAuthService.getCurrentUser().getId();
    this.currentTasks = playerApi.getCurrentTasks(userId);

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
    broadcasterRegistration = taskUpdatesTopic.subscribe(
        userId, n -> ui.access(() -> reloadTasks(userId, n.getMessage()))
    );
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    broadcasterRegistration.remove();
    broadcasterRegistration = null;
  }

  private void showTasks(List<PlayerTaskInfo> tasks) {
    getContent().removeAll();
    this.currentTasks = new ArrayList<>(tasks);

    // Кнопка переключения режима
    var toggleMode = new Button(isSwipeMode ? "Режим кнопок" : "Режим свайпа", e -> {
      isSwipeMode = !isSwipeMode;
      refreshTasksDisplay();
    });

    getContent().add(toggleMode);
    refreshTasksDisplay();
  }

  private void refreshTasksDisplay() {
    // Удаляем только компоненты задач, оставляя кнопку переключения
    getContent().getChildren()
        .filter(c -> !(c instanceof Button))
        .forEach(getContent()::remove);

    // Добавляем соответствующий компонент
    if (isSwipeMode) {
      getContent().add(new SwipedComponent(currentTasks, this));
    } else {
      getContent().add(new ButtonedTasksComponent(currentTasks, this));
    }

    // Обновляем текст кнопки
    getContent().getChildren()
        .filter(Button.class::isInstance)
        .findFirst()
        .ifPresent(b -> ((Button)b).setText(isSwipeMode ? "Режим кнопок" : "Режим свайпа"));
  }

  @ClientCallable
  public void completeTask(String taskId) {
    handleTaskCompletion(taskId, true);
  }

  @ClientCallable
  public void skipTask(String taskId) {
    handleTaskCompletion(taskId, false);
  }

  private void handleTaskCompletion(String taskId, boolean completed) {
    currentTasks.removeIf(task -> task.getTaskInfo().getId().equals(taskId));
    refreshTasksDisplay();
    Notification.show(completed ? "Задача завершена!" : "Задача пропущена");
  }

  private void showChooseTaskTopics(long userId) {
    var container = new VerticalLayout();
    container.setWidth("100%");

    var checkboxes = StreamEx.of(TaskTopic.values())
        .remove(UNRECOGNIZED::equals)
        .map(TaskTopicMapper::to)
        .peek(container::add)
        .toList();

    var submitButton = new Button("Send", e ->
        handleSelectedTopics(userId, StreamEx.of(checkboxes)
            .filter(AbstractField::getValue)
            .map(TaskTopicMapper::from)
            .toList())
    );

    container.add(submitButton);
    getContent().add(container);
  }

  private void handleSelectedTopics(long userId, List<TaskTopic> selectedTopics) {
    playerApi.savePlayerTopics(userId, selectedTopics);
    playerApi.generateTasks(userId);
    reloadTasks(userId, "Your tasks have started to be generated!");
  }

  private void reloadTasks(long userId, String message) {
    this.currentTasks = playerApi.getCurrentTasks(userId);
    showTasks(currentTasks);
    Notification.show(message);
  }

  private static class TaskTopicMapper {
    private static final String SPACE = " ";
    private static final String UNDERSCORE = "_";

    public static TaskTopic from(Checkbox checkbox) {
      return TaskTopic.valueOf(checkbox.getLabel().replace(SPACE, UNDERSCORE).toUpperCase());
    }

    public static Checkbox to(TaskTopic topic) {
      return new Checkbox(topic.name().replace(UNDERSCORE, SPACE).toLowerCase());
    }
  }
}