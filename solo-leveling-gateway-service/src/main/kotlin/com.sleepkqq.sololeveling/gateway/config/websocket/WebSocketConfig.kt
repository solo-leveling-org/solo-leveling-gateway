package com.sleepkqq.sololeveling.gateway.config.websocket

import com.sleepkqq.sololeveling.gateway.config.security.CorsProperties
import com.sleepkqq.sololeveling.gateway.config.security.JwtHandshakeInterceptor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Suppress("unused")
@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(RabbitMqStompProperties::class)
class WebSocketConfig(
	private val jwtHandshakeInterceptor: JwtHandshakeInterceptor,
	private val rabbitMqStompProperties: RabbitMqStompProperties,
	private val corsProperties: CorsProperties
) : WebSocketMessageBrokerConfigurer {

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.addInterceptors(jwtHandshakeInterceptor)
	}

	override fun configureMessageBroker(registry: MessageBrokerRegistry) {
		registry.enableStompBrokerRelay(
			*rabbitMqStompProperties.destinationPrefixes.toTypedArray()
		)
			.setRelayHost(rabbitMqStompProperties.host)
			.setRelayPort(rabbitMqStompProperties.port)
			.setClientLogin(rabbitMqStompProperties.username)
			.setClientPasscode(rabbitMqStompProperties.password)
			.setSystemLogin(rabbitMqStompProperties.username)
			.setSystemPasscode(rabbitMqStompProperties.password)
			.setUserDestinationBroadcast(rabbitMqStompProperties.userDestinationBroadcast)
			.setUserRegistryBroadcast(rabbitMqStompProperties.userRegistryBroadcast)

		registry.setApplicationDestinationPrefixes(
			*rabbitMqStompProperties.applicationDestinationPrefixes.toTypedArray()
		)
		registry.setUserDestinationPrefix(rabbitMqStompProperties.userDestinationPrefix)
	}
}
