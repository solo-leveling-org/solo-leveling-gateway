package com.sleepkqq.sololeveling.ui.view.auth

import com.sleepkqq.sololeveling.ui.exception.CustomErrorHandler
import com.sleepkqq.sololeveling.ui.service.TgAuthService
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinSession
import com.vaadin.flow.server.auth.AnonymousAllowed
import org.springframework.context.MessageSource

@PageTitle("Authentication")
@Route("auth")
@Uses(Icon::class)
@AnonymousAllowed
class AuthView(
	tgAuthService: TgAuthService,
	messageSource: MessageSource
) : Composite<VerticalLayout>() {

	init {
		content.width = "100%"
		content.style.set("flex-grow", "1")
		content.justifyContentMode = JustifyContentMode.CENTER
		content.alignItems = FlexComponent.Alignment.CENTER

		val customErrorHandler = CustomErrorHandler(tgAuthService, messageSource)
		VaadinSession.getCurrent().errorHandler = customErrorHandler

		val telegramAuthComponent = TelegramAuthComponent(tgAuthService)
		content.add(telegramAuthComponent)
	}
}
