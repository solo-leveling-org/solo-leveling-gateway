package com.sleepkqq.sololeveling.gateway.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.cors")
data class CorsProperties (
	val hosts: List<String>,
	val methods: List<String>
)