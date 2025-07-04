package com.sleepkqq.sololeveling.ui.kafka

import com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import com.sleepkqq.sololeveling.ui.broadcast.TaskUpdatesTopic
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ReceiveNotificationConsumer(
	private val taskUpdatesTopic: TaskUpdatesTopic
) {

	private val log = LoggerFactory.getLogger(ReceiveNotificationConsumer::class.java)

	@KafkaListener(
		topics = [KafkaTaskTopics.UI_NOTIFICATION_TOPIC],
		groupId = KafkaGroupIds.UI_GROUP_ID
	)
	fun listen(event: ReceiveNotificationEvent) {
		log.info(">> Received notification | transactionId={}", event.transactionId)
		taskUpdatesTopic.publish(event.userId, event.notification)
	}
}
