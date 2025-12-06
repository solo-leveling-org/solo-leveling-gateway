package com.sleepkqq.sololeveling.gateway.service.websocket

import com.sleepkqq.sololeveling.gateway.config.websocket.RabbitMqStompProperties
import com.sleepkqq.sololeveling.gateway.dto.*
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class NotificationService(
	private val messagingTemplate: SimpMessagingTemplate,
	private val rabbitMqStompProperties: RabbitMqStompProperties
) {

	fun sendUserNotification(userId: Long, notification: WsNotification) {
		val wsMessage = WsMessage()
			.payload(notification)
			.timestamp(OffsetDateTime.now(ZoneOffset.UTC))

		messagingTemplate.convertAndSendToUser(
			userId.toString(),
			rabbitMqStompProperties.userNotificationDestination,
			wsMessage
		)
	}
}
