package com.sleepkqq.sololeveling.gateway.kafka

import com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import com.sleepkqq.sololeveling.gateway.mapper.AvroMapper
import com.sleepkqq.sololeveling.gateway.service.websocket.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class ReceiveNotificationConsumer(
	private val notificationService: NotificationService,
	private val avroMapper: AvroMapper
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@KafkaListener(
		topics = [KafkaTaskTopics.UI_NOTIFICATION_TOPIC],
		groupId = KafkaGroupIds.UI_GROUP_ID
	)
	fun listen(event: ReceiveNotificationEvent, ack: Acknowledgment) {
		log.info(">> Received notification | transactionId={}", event.transactionId)

		notificationService.sendUserNotification(avroMapper.map(event))

		ack.acknowledge()
	}
}
