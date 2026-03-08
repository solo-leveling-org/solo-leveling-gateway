package com.soloist.gateway.dto.auth

import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TgWebAppData(
	val queryId: String?,
	val user: TgUserData,
	val authDate: OffsetDateTime,
	val hash: String,
	val signature: String?,
	val startParam: String?
)
