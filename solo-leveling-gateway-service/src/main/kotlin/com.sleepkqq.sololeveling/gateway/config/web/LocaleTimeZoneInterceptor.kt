package com.sleepkqq.sololeveling.gateway.config.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.ZoneOffset
import java.util.Locale
import java.util.TimeZone

@Component
class LocaleTimeZoneInterceptor : HandlerInterceptor {

	private companion object {
		const val ACCEPT_LANGUAGE_HEADER = "Accept-Language"
		const val TIME_ZONE_HEADER = "X-TimeZone"
	}

	override fun preHandle(
		request: HttpServletRequest,
		response: HttpServletResponse,
		handler: Any
	): Boolean {
		val locale = request.getHeader(ACCEPT_LANGUAGE_HEADER)
			?.let { Locale.forLanguageTag(it.split(",").first().trim()) }
			?: Locale.ENGLISH

		val timeZone = request.getHeader(TIME_ZONE_HEADER)
			?.let { TimeZone.getTimeZone(it) }
			?: TimeZone.getTimeZone(ZoneOffset.UTC)

		LocaleContextHolder.setLocaleContext(
			SimpleTimeZoneAwareLocaleContext(locale, timeZone)
		)

		return true
	}

	override fun afterCompletion(
		request: HttpServletRequest,
		response: HttpServletResponse,
		handler: Any,
		ex: Exception?
	) {
		LocaleContextHolder.resetLocaleContext()
	}
}