package com.sleepkqq.sololeveling.ui.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TgWebAppData(
	val authDate: LocalDateTime,
	val chatType: String?,
	val chatInstance: String?,
	val hash: String,
	val signature: String?,
	val user: TgUserData
)
