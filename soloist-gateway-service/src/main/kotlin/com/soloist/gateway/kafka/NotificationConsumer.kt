package com.soloist.gateway.kafka

import com.soloist.avro.config.consumer.AbstractKafkaConsumer
import com.soloist.avro.constants.KafkaTaskTopics
import com.soloist.avro.idempotency.IdempotencyService
import com.soloist.avro.notification.NotificationEvent
import com.soloist.gateway.mapper.AvroMapper
import com.soloist.gateway.service.websocket.NotificationService
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
