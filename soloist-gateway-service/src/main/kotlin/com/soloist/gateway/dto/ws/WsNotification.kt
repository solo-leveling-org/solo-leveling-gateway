package com.soloist.gateway.dto.ws

data class WsNotification(
	val message: String?,
	val type: WsNotificationType,
	val source: WsNotificationSource
)
