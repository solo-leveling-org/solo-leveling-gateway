package com.soloist.gateway.dto.ws

import java.time.OffsetDateTime

data class WsMessage(
	val payload: WsNotification,
	val timestamp: OffsetDateTime
)
