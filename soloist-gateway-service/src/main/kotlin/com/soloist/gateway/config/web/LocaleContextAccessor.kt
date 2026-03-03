package com.soloist.gateway.config.web

import io.micrometer.context.ThreadLocalAccessor
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext
import java.util.Locale
import java.util.TimeZone

class LocaleContextAccessor : ThreadLocalAccessor<LocaleContextAccessor.LocaleContextSnapshot> {

	private companion object {
		const val KEY = "localeContext"
	}

	override fun key() = KEY

	override fun getValue() = LocaleContextSnapshot(
		LocaleContextHolder.getLocale(),
		LocaleContextHolder.getTimeZone()
	)

	override fun setValue(value: LocaleContextSnapshot) {
		LocaleContextHolder.setLocaleContext(
			SimpleTimeZoneAwareLocaleContext(value.locale, value.timeZone)
		)
	}

	override fun setValue() = LocaleContextHolder.resetLocaleContext()

	data class LocaleContextSnapshot(val locale: Locale, val timeZone: TimeZone)
}
