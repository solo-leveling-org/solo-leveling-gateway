package com.sleepkqq.sololeveling.gateway.config.security

import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.gateway.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthenticationFilter(
	private val jwtService: JwtService
) : OncePerRequestFilter() {

	private companion object {
		const val BEARER_PREFIX = "Bearer "
	}

	private val log = LoggerFactory.getLogger(javaClass)

	@Throws(ServletException::class, IOException::class)
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val authHeader = request.getHeader(AUTHORIZATION)
		if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
			filterChain.doFilter(request, response)
			return
		}

		try {
			val jwt: String = authHeader.substring(BEARER_PREFIX.length)

			val user = UserData.fromTgUser(jwtService.extractTgUser(jwt))

			if (SecurityContextHolder.getContext().authentication == null) {
				val authentication = UsernamePasswordAuthenticationToken(user, jwt, user.authorities)
				authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
				SecurityContextHolder.getContext().authentication = authentication
			}

			filterChain.doFilter(request, response)
		} catch (e: Exception) {
			log.error(e.message)
			response.status = HttpServletResponse.SC_UNAUTHORIZED
			response.contentType = APPLICATION_JSON_VALUE
			response.writer.write(e.toString())
		}
	}
}
