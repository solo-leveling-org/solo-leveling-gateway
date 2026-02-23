package com.soloist.gateway.config.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.jwt")
class JwtProperties(
	val secret: String,
	val accessLifetime: Long,
	val refreshLifetime: Long
)
