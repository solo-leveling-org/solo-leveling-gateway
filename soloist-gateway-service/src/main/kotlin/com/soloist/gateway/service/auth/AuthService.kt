package com.soloist.gateway.service.auth

import com.soloist.gateway.dto.RestJwtToken
import com.soloist.gateway.dto.RestLoginResponse
import com.soloist.gateway.dto.RestTgAuthData
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

	fun login(tgAuthData: RestTgAuthData): RestLoginResponse {
		if (!tgHashService.checkHash(tgAuthData)) {
			throw LocalizationException(LocalizationMessage.ERROR_AUTH_HASH)
		}

		return jwtService.generateToken(tgAuthData.tgWebAppData.user)
	}

	fun refresh(refreshToken: String): RestJwtToken =
		jwtService.generateAccessTokenFromRefreshToken(refreshToken)

	fun getCurrentUser(): UserData =
		SecurityContextHolder.getContext().authentication!!.principal as UserData
}
