package com.sleepkqq.sololeveling.service;

import com.sleepkqq.sololeveling.entity.task.Task;
import com.sleepkqq.sololeveling.repository.TaskRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;

  public Task saveTask(Task task) {
    return taskRepository.save(task);
  }

  public List<Task> findByTitle(String title) {
    return taskRepository.findByTitle(title);
  }

  public List<Task> findByExperienceBetween(int minExperience, int maxExperience) {
    return taskRepository.findByExperienceBetween(minExperience, maxExperience);
  }

  public List<Task> findAll() {
    return StreamEx.of(taskRepository.findAll().spliterator()).toList();
  }

  public void deleteTask(String id) {
    taskRepository.deleteById(id);
  }
}
