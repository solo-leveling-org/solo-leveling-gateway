package com.sleepkqq.sololeveling.api;

import com.sleepkqq.sololeveling.proto.task.GetTasksRequest;
import com.sleepkqq.sololeveling.proto.task.Task;
import com.sleepkqq.sololeveling.proto.task.TaskServiceGrpc.TaskServiceBlockingStub;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// todo: add retry
public class TaskApi {

  private final TaskServiceBlockingStub taskStub;

  public List<Task> getTasks(Collection<String> taskIds) {
    var response = taskStub.getTasks(GetTasksRequest.newBuilder()
        .addAllTaskId(taskIds)
        .build());

    if (!response.getSuccess()) {
      throw new IllegalArgumentException("Failed to get tasks, taskIds=" + taskIds);
    }

    return response.getTaskList();
  }
}
