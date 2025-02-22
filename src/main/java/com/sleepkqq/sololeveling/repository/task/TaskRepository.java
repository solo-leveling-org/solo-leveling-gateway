package com.sleepkqq.sololeveling.repository.task;

import com.sleepkqq.sololeveling.model.task.Task;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TaskRepository extends ElasticsearchRepository<Task, String> {

  List<Task> findByTitle(String title);

  List<Task> findByExperienceBetween(int minExperience, int maxExperience);
}
