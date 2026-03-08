package com.soloist.gateway.dto.auth

import java.time.OffsetDateTime

data class JwtToken(
	val token: String,
	val expiration: OffsetDateTime,
	val type: JwtTokenType
)
