package com.soloist.gateway.config.rabbitmq

import com.soloist.event.NotificationEvent
import com.soloist.gateway.dto.ws.WsNotification
import com.soloist.gateway.dto.ws.WsNotificationSource
import com.soloist.gateway.dto.ws.WsNotificationType
import com.soloist.gateway.service.websocket.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class NotificationListener(
	private val notificationService: NotificationService
) {

	private val log = LoggerFactory.getLogger(javaClass)

	@RabbitListener(queues = [RabbitMqConfig.NOTIFICATION_QUEUE])
	fun onNotification(event: NotificationEvent) {
		log.debug("Received notification event: userId={}, type={}, source={}", event.userId, event.type, event.source)

		val notification = WsNotification(
			message = event.message,
			type = WsNotificationType.valueOf(event.type.name),
			source = WsNotificationSource.valueOf(event.source.name)
		)
		notificationService.sendUserNotification(event.userId, notification)
	}
}
