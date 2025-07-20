package com.sleepkqq.sololeveling.gateway.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.jwt")
class JwtProperties(
	val secret: String,
	val accessLifetime: Long,
	val refreshLifetime: Long
)