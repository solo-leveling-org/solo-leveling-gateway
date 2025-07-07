package com.sleepkqq.sololeveling.ui.service

import com.sleepkqq.sololeveling.ui.api.UserApi
import com.sleepkqq.sololeveling.ui.localization.LocalizationException
import com.sleepkqq.sololeveling.ui.localization.LocalizationMessage
import com.sleepkqq.sololeveling.ui.model.TgAuthData
import com.sleepkqq.sololeveling.ui.model.UserData
import com.sleepkqq.sololeveling.ui.view.home.HomeView
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service

@Service
class TgAuthService(
	private val userApi: UserApi,
	private val tgHashService: TgHashService
) {

	private val log = LoggerFactory.getLogger(TgAuthService::class.java)

	fun authenticate(tgAuthData: TgAuthData) {
		if (this.isLoggedIn) {
			UI.getCurrent().navigate(HomeView::class.java)
			return
		}

		val tgWebAppData = tgAuthData.tgWebAppData
		if (!tgHashService.checkHash(tgAuthData.initData, tgWebAppData.hash)) {
			log.error("Invalid hash, received data: '{}'", tgAuthData)
			throw LocalizationException(LocalizationMessage.ERROR_AUTH_HASH)
		}

		val userData = UserData.fromTgUser(tgWebAppData.user)
		userApi.authUser(userData)

		authentication = UsernamePasswordAuthenticationToken.authenticated(
			userData,
			tgWebAppData.hash,
			userData.authorities
		)
		UI.getCurrent().page.reload()

		log.info("User '{}' authenticated successfully", userData.username)
	}

	fun logout() {
		authentication = null
		UI.getCurrent().page.reload()
	}

	val isLoggedIn: Boolean
		get() = authentication != null

	fun findCurrentUser(): UserData? {
		return authentication
			?.principal
			?.takeIf { it is UserData }
			?.let { it as UserData }
	}

	val currentUser: UserData
		get() = findCurrentUser()
			?: throw LocalizationException(LocalizationMessage.ERROR_AUTH_REQUIRED)

	private var authentication: Authentication?
		get() = context.authentication
		set(authentication) {
			context.authentication = authentication
			VaadinSession.getCurrent()
				.session
				.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context)
		}

	private val context: SecurityContext
		get() = SecurityContextHolder.getContext()
}