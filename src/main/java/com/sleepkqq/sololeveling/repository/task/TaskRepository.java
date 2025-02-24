package com.sleepkqq.sololeveling.repository.task;

import com.sleepkqq.sololeveling.model.task.Task;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, UUID> {

  List<Task> findByTitle(String title);

  List<Task> findByExperienceBetween(int minExperience, int maxExperience);
}
