package com.sleepkqq.sololeveling.gateway.kafka

import com.sleepkqq.sololeveling.avro.config.consumer.AbstractKafkaConsumer
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.idempotency.IdempotencyService
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import com.sleepkqq.sololeveling.gateway.mapper.AvroMapper
import com.sleepkqq.sololeveling.gateway.service.websocket.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReceiveNotificationConsumer(
	private val notificationService: NotificationService,
	private val avroMapper: AvroMapper,
	idempotencyService: IdempotencyService
) : AbstractKafkaConsumer<ReceiveNotificationEvent>(
	idempotencyService = idempotencyService,
	log = LoggerFactory.getLogger(ReceiveNotificationConsumer::class.java)
) {

	@Transactional
	@RetryableTopic
	@KafkaListener(
		topics = [KafkaTaskTopics.UI_NOTIFICATION_TOPIC],
		groupId = $$"${spring.kafka.avro.group-id}"
	)
	fun listen(event: ReceiveNotificationEvent) {
		consumeWithIdempotency(event)
	}

	override fun getTxId(event: ReceiveNotificationEvent): String = event.txId

	override fun processEvent(event: ReceiveNotificationEvent) {
		log.info(">> Received notification | txId={}", event.txId)

		notificationService.sendUserNotification(event.userId, avroMapper.map(event.notification))
	}
}
