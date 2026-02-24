package com.soloist.gateway.service.websocket

import com.soloist.gateway.config.websocket.RabbitMqStompProperties
import com.soloist.gateway.dto.WsMessage
import com.soloist.gateway.dto.WsNotification
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
