package com.sleepkqq.sololeveling.gateway.config.websocket

import com.sleepkqq.sololeveling.gateway.config.security.CorsProperties
import com.sleepkqq.sololeveling.gateway.config.security.JwtHandshakeInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Suppress("unused")
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
	private val jwtHandshakeInterceptor: JwtHandshakeInterceptor,
	private val rabbitMqStompProperties: RabbitMqStompProperties,
	private val corsProperties: CorsProperties
) : WebSocketMessageBrokerConfigurer {

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns(*corsProperties.hosts.toTypedArray())
			.addInterceptors(jwtHandshakeInterceptor)
			.withSockJS()
	}

	override fun configureMessageBroker(registry: MessageBrokerRegistry) {
		registry.enableStompBrokerRelay("/user", "/topic")
			.setRelayHost(rabbitMqStompProperties.host)
			.setRelayPort(rabbitMqStompProperties.port)
			.setClientLogin(rabbitMqStompProperties.username)
			.setClientPasscode(rabbitMqStompProperties.password)
			.setSystemLogin(rabbitMqStompProperties.username)
			.setSystemPasscode(rabbitMqStompProperties.password)

		registry.setApplicationDestinationPrefixes("/app")
		registry.setUserDestinationPrefix("/user")
	}
}
