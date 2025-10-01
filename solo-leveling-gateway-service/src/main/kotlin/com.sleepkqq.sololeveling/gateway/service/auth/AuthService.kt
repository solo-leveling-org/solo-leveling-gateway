package com.sleepkqq.sololeveling.gateway.service.auth

import com.sleepkqq.sololeveling.gateway.dto.RestJwtToken
import com.sleepkqq.sololeveling.gateway.dto.RestLoginResponse
import com.sleepkqq.sololeveling.gateway.dto.RestTgAuthData
import com.sleepkqq.sololeveling.gateway.localization.LocalizationException
import com.sleepkqq.sololeveling.gateway.localization.LocalizationMessage
import com.sleepkqq.sololeveling.gateway.model.UserData
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
		SecurityContextHolder.getContext().authentication.principal as UserData
}
