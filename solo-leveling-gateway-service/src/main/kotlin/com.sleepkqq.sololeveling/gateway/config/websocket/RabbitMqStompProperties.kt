package com.sleepkqq.sololeveling.gateway.config.websocket

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.rabbitmq.stomp")
class RabbitMqStompProperties(
	val host: String,
	val port: Int,
	val username: String,
	val password: String,
	val destinationPrefixes: List<String>,
	val userDestinationPrefix: String,
	val userDestinationBroadcast: String,
	val userNotificationDestination: String,
	val applicationDestinationPrefixes: List<String>
)
