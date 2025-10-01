package com.sleepkqq.sololeveling.gateway.config

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
	private val jwtHandshakeInterceptor: JwtHandshakeInterceptor
) : WebSocketMessageBrokerConfigurer {

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.addInterceptors(jwtHandshakeInterceptor)
			.withSockJS()
	}

	override fun configureMessageBroker(registry: MessageBrokerRegistry) {
		registry.enableSimpleBroker("/user")
		registry.setApplicationDestinationPrefixes("/app")
		registry.setUserDestinationPrefix("/user")
	}
}