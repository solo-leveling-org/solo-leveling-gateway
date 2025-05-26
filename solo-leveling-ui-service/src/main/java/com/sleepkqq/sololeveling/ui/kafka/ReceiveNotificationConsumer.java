package com.sleepkqq.sololeveling.ui.kafka;

import static com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds.UI_GROUP_ID;
import static com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics.UI_NOTIFICATION_TOPIC;

import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent;
import com.sleepkqq.sololeveling.ui.broadcast.TaskUpdatesTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveNotificationConsumer {

  private final TaskUpdatesTopic taskUpdatesTopic;

  @KafkaListener(topics = UI_NOTIFICATION_TOPIC, groupId = UI_GROUP_ID)
  public void listen(ReceiveNotificationEvent event) {
    log.info(">> Received notification | transactionId={}", event.getTransactionId());
    taskUpdatesTopic.publish(event.getUserId(), event.getNotification());
  }
}
