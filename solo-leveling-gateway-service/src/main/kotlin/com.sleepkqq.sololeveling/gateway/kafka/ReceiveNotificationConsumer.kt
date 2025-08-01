package com.sleepkqq.sololeveling.gateway.kafka

import com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class ReceiveNotificationConsumer {

	private val log = LoggerFactory.getLogger(javaClass)

	@KafkaListener(
		topics = [KafkaTaskTopics.UI_NOTIFICATION_TOPIC],
		groupId = KafkaGroupIds.UI_GROUP_ID
	)
	fun listen(event: ReceiveNotificationEvent) {
		log.info(">> Received notification | transactionId={}", event.transactionId)
	}
}
