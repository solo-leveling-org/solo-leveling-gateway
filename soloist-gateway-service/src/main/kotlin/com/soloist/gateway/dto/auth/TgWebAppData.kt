package com.soloist.gateway.dto.auth

import java.time.OffsetDateTime

data class TgWebAppData(
	val queryId: String?,
	val user: TgUserData,
	val authDate: OffsetDateTime,
	val hash: String,
	val signature: String?,
	val startParam: String?
)
