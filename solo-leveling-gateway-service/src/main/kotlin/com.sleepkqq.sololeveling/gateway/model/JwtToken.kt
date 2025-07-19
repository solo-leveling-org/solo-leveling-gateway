package com.sleepkqq.sololeveling.gateway.model

import java.time.LocalDateTime

class JwtToken(
	val token: String,
	val expiration: LocalDateTime,
	val type: JwtTokenType
)
