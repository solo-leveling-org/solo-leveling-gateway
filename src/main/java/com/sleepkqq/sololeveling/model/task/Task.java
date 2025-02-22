package com.sleepkqq.sololeveling.model.task;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Builder
@Document(indexName = "tasks")
public class Task {

  @Id
  private final String id;

  @Field(type = FieldType.Text)
  private final String title;

  @Field(type = FieldType.Text)
  private final String description;

  @Field(type = FieldType.Integer)
  private final int experience;

  @Enumerated(EnumType.STRING)
  @Field(type = FieldType.Keyword)
  private final Rarity rarity;

  @Enumerated(EnumType.STRING)
  @Field(type = FieldType.Keyword)
  private final TaskStatus status;

  @Field(type = FieldType.Date)
  private final LocalDateTime createdAt;

  @Field(type = FieldType.Date)
  private final LocalDateTime updatedAt;
}
