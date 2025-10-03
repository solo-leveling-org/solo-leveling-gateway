package com.sleepkqq.sololeveling.gateway.config.security

import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.gateway.service.auth.JwtService
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class JwtHandshakeInterceptor(
	private val jwtService: JwtService
) : HandshakeInterceptor {

	private companion object {
		const val TOKEN_PARAM = "token"
	}

	private val log = LoggerFactory.getLogger(javaClass)

	override fun beforeHandshake(
		request: ServerHttpRequest,
		response: ServerHttpResponse,
		wsHandler: WebSocketHandler,
		attributes: MutableMap<String, Any>
	): Boolean {

		if (request !is ServletServerHttpRequest) {
			return false
		}

		val token = request.servletRequest.getParameter(TOKEN_PARAM)
		if (!token.isNullOrBlank()) {
			return handleJwtHandshake(token)
		}

		return false
	}

	private fun handleJwtHandshake(jwt: String): Boolean {
		return try {
			val tgUser = jwtService.extractTgUser(jwt)
			val user = UserData.fromTgUser(tgUser)

			val authentication = UsernamePasswordAuthenticationToken(user, jwt, user.authorities)
			SecurityContextHolder.getContext().authentication = authentication

			true

		} catch (e: ExpiredJwtException) {
			log.info("Expired JWT token during WebSocket handshake: ${e.message}")
			false

		} catch (e: Exception) {
			log.error("JWT authentication failed during WebSocket handshake", e)
			false
		}
	}

	override fun afterHandshake(
		request: ServerHttpRequest,
		response: ServerHttpResponse,
		wsHandler: WebSocketHandler,
		exception: Exception?
	) {
		// nothing to do
	}
}
