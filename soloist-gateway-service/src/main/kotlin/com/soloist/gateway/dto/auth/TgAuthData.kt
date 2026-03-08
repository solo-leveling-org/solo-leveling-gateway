package com.soloist.gateway.dto.auth

import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TgAuthData(
	val tgWebAppData: TgWebAppData,
	val initData: String
)
