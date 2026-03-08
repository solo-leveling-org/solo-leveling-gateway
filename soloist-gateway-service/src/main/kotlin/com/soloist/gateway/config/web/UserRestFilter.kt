package com.soloist.gateway.config.web

import com.soloist.config.interceptor.UserContextHolder
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.ZoneOffset
import java.util.*

@Component
class UserRestFilter : OncePerRequestFilter() {

	private companion object {
		const val TIME_ZONE_HEADER = "X-TimeZone"
	}

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val locale = request.locale ?: Locale.ENGLISH
		val timeZone = request.getHeader(TIME_ZONE_HEADER)
			?.let { TimeZone.getTimeZone(it) }
			?: TimeZone.getTimeZone(ZoneOffset.UTC)

		LocaleContextHolder.setLocaleContext(SimpleTimeZoneAwareLocaleContext(locale, timeZone))

		try {
			filterChain.doFilter(request, response)
		} finally {
			LocaleContextHolder.resetLocaleContext()
			UserContextHolder.clear()
		}
	}
}
