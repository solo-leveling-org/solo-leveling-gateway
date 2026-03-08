package com.soloist.gateway.service.auth

import com.soloist.gateway.dto.auth.JwtToken
import com.soloist.gateway.dto.auth.LoginResponse
import com.soloist.gateway.dto.auth.TgAuthData
import com.soloist.gateway.localization.LocalizationException
import com.soloist.gateway.localization.LocalizationMessage
import com.soloist.gateway.model.UserData
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
	private val jwtService: JwtService,
	private val tgHashService: TgHashService
) {

	fun login(tgAuthData: TgAuthData): LoginResponse {
		if (!tgHashService.checkHash(tgAuthData)) {
			throw LocalizationException(LocalizationMessage.ERROR_AUTH_HASH)
		}

		return jwtService.generateToken(tgAuthData.tgWebAppData.user)
	}

	fun refresh(refreshToken: String): JwtToken =
		jwtService.generateAccessTokenFromRefreshToken(refreshToken)

	fun getCurrentUser(): UserData =
		SecurityContextHolder.getContext().authentication!!.principal as UserData
}
