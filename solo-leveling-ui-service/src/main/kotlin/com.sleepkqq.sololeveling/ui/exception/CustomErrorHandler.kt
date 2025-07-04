package com.sleepkqq.sololeveling.ui.exception

import com.sleepkqq.sololeveling.ui.localization.LocalizationException
import com.sleepkqq.sololeveling.ui.localization.LocalizationMessage
import com.sleepkqq.sololeveling.ui.service.TgAuthService
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.server.ErrorEvent
import com.vaadin.flow.server.ErrorHandler
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import java.util.Locale

class CustomErrorHandler(
	private val tgAuthService: TgAuthService,
	private val messageSource: MessageSource
) : ErrorHandler {

	private val log = LoggerFactory.getLogger(CustomErrorHandler::class.java)

	override fun error(errorEvent: ErrorEvent) {
		log.error("Error occurred", errorEvent.throwable)

		val exception = errorEvent.throwable
		val locale = tgAuthService.findCurrentUser()
			?.locale
			?: Locale.ENGLISH

		UI.getCurrent()
			?.let { it.access { showExceptionNotification(exception, locale) } }
	}

	private fun showExceptionNotification(exception: Throwable, locale: Locale) {
		val path = if (exception is LocalizationException)
			exception.localizationMessage.path
		else
			LocalizationMessage.ERROR_UNEXPECTED.path

		Notification.show(messageSource.getMessage(path, null, locale))
	}
}
