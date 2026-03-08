package com.soloist.gateway.config.security

import com.soloist.config.interceptor.UserContextHolder
import com.soloist.gateway.model.UserData
import com.soloist.gateway.service.auth.JwtService
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.Collections
import java.util.Enumeration

@Component
class JwtAuthenticationFilter(
	private val jwtService: JwtService,
	private val securityContextRepository: RequestAttributeSecurityContextRepository
) : OncePerRequestFilter() {

	private companion object {
		const val BEARER_PREFIX = "Bearer "
		const val USER_ID_HEADER = "X-UserId"
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
			val jwt = authHeader.substring(BEARER_PREFIX.length)
			val user = UserData.fromTgUser(jwtService.extractTgUser(jwt))

			if (SecurityContextHolder.getContext().authentication == null) {
				val authentication = UsernamePasswordAuthenticationToken(user, jwt, listOf())
				authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

				val context = SecurityContextHolder.createEmptyContext()
				context.authentication = authentication
				SecurityContextHolder.setContext(context)
				securityContextRepository.saveContext(context, request, response)

				UserContextHolder.setUserId(user.id)

				val wrappedRequest = HeaderInjectionRequestWrapper(request, user.id)
				filterChain.doFilter(wrappedRequest, response)
			} else {
				filterChain.doFilter(request, response)
			}

		} catch (e: Exception) {
			if (e is ExpiredJwtException) {
				log.info("Expired JWT token: ${e.message}")
			} else {
				log.error("JWT authentication failed", e)
			}

			response.status = HttpServletResponse.SC_UNAUTHORIZED
			response.contentType = APPLICATION_JSON_VALUE
			response.writer.write("""{"error": "Unauthorized", "message": "${e.message}"}""")
		}
	}

	private class HeaderInjectionRequestWrapper(
		request: HttpServletRequest,
		private val userId: Long
	) : HttpServletRequestWrapper(request) {

		override fun getHeader(name: String?): String? {
			return when (name) {
				USER_ID_HEADER -> userId.toString()
				else -> super.getHeader(name)
			}
		}

		override fun getHeaders(name: String?): Enumeration<String> {
			return when (name) {
				USER_ID_HEADER -> Collections.enumeration(listOf(userId.toString()))
				else -> super.getHeaders(name)
			}
		}
	}
}
