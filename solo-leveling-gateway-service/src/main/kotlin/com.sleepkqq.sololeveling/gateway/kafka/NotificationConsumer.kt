package com.sleepkqq.sololeveling.gateway.kafka

import com.sleepkqq.sololeveling.avro.config.consumer.AbstractKafkaConsumer
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.idempotency.IdempotencyService
import com.sleepkqq.sololeveling.avro.notification.NotificationEvent
import com.sleepkqq.sololeveling.gateway.mapper.AvroMapper
import com.sleepkqq.sololeveling.gateway.service.websocket.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service

@Service
class NotificationConsumer(
	private val notificationService: NotificationService,
	private val avroMapper: AvroMapper,
	idempotencyService: IdempotencyService
) : AbstractKafkaConsumer<NotificationEvent>(
	idempotencyService = idempotencyService,
	log = LoggerFactory.getLogger(NotificationConsumer::class.java)
) {

	@RetryableTopic
	@KafkaListener(
		topics = [KafkaTaskTopics.UI_NOTIFICATION_TOPIC],
		groupId = $$"${spring.kafka.avro.group-id}"
	)
	fun listen(event: NotificationEvent) {
		consumeWithIdempotency(event)
	}

	override fun getTxId(event: NotificationEvent): String = event.txId

	override fun processEvent(event: NotificationEvent) {
		notificationService.sendUserNotification(
			event.userId,
			avroMapper.map(event.notification)
		)
	}
}
