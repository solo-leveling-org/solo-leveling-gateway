package com.sleepkqq.sololeveling.model.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(generator = "UUID")
  @Column(updatable = false, nullable = false)
  private UUID id;

  private String title;

  private String description;

  private int experience;

  @Enumerated(EnumType.STRING)
  private Rarity rarity;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @Column(updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Task(String title, String description, int experience, Rarity rarity) {
    this.title = title;
    this.description = description;
    this.experience = experience;
    this.rarity = rarity;
    this.status = TaskStatus.IN_PROGRESS;
  }

  public void complete() {
    this.status = TaskStatus.COMPLETED;
  }

  public void skip() {
    this.status = TaskStatus.SKIPPED;
  }
}
