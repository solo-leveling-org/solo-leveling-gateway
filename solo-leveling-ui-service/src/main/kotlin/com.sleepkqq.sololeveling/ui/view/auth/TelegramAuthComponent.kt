package com.sleepkqq.sololeveling.ui.view.auth

import com.sleepkqq.sololeveling.ui.model.TgAuthData
import com.sleepkqq.sololeveling.ui.service.TgAuthService
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.react.ReactAdapterComponent

@Tag("telegram-auth")
@JsModule("./component/TelegramAuthComponent.js")
class TelegramAuthComponent(
	private val tgAuthService: TgAuthService
) : ReactAdapterComponent() {

	companion object {

		private const val TG_AUTH_DATA = "tgAuthData"
	}

	init {
		addAuthListener()
	}

	private fun addAuthListener() =
		addStateChangeListener(TG_AUTH_DATA, TgAuthData::class.java, tgAuthService::authenticate)
}