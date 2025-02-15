package com.sleepkqq.sololeveling.entity.task;

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

  @Field(type = FieldType.Text, name = "title")
  private final String title;

  @Field(type = FieldType.Text, name = "description")
  private final String description;

  @Field(type = FieldType.Integer, name = "experience")
  private final int experience;

  @Enumerated(EnumType.STRING)
  @Field(type = FieldType.Keyword, name = "rarity")
  private final Rarity rarity;

  @Enumerated(EnumType.STRING)
  @Field(type = FieldType.Keyword, name = "status")
  private final TaskStatus status;

  @Field(type = FieldType.Date, name = "createAt")
  private final LocalDateTime createdAt;

  @Field(type = FieldType.Date, name = "updatedAt")
  private final LocalDateTime updatedAt;
}
