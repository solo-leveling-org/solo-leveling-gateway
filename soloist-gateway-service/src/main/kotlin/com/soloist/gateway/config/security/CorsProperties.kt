package com.soloist.gateway.config.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.cors")
data class CorsProperties (
	val hosts: List<String>,
	val methods: List<String>
)
