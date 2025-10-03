package com.sleepkqq.sololeveling.gateway.service.websocket

import com.sleepkqq.sololeveling.gateway.dto.*
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NotificationService(
	private val messagingTemplate: SimpMessagingTemplate
) {

	fun sendUserNotification(notification: WsUserNotification) {
		val wsMessage = WsMessage()
			.payload(notification)
			.timestamp(LocalDateTime.now())

		messagingTemplate.convertAndSendToUser(
			"${notification.userId}",
			"/queue/notifications",
			wsMessage
		)
	}
}
