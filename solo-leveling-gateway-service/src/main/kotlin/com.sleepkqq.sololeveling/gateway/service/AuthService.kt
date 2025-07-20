package com.sleepkqq.sololeveling.gateway.service

import com.sleepkqq.sololeveling.gateway.dto.JwtResponse
import com.sleepkqq.sololeveling.gateway.dto.JwtToken
import com.sleepkqq.sololeveling.gateway.dto.TgAuthData
import com.sleepkqq.sololeveling.gateway.localization.LocalizationException
import com.sleepkqq.sololeveling.gateway.localization.LocalizationMessage
import org.springframework.stereotype.Service

@Service
class AuthService(
	private val jwtService: JwtService,
	private val tgHashService: TgHashService
) {

	fun login(tgAuthData: TgAuthData): JwtResponse {
		if (!tgHashService.checkHash(tgAuthData.initData, tgAuthData.tgWebAppData.hash)) {
			throw LocalizationException(LocalizationMessage.ERROR_AUTH_HASH)
		}

		return jwtService.generateToken(tgAuthData.tgWebAppData.user)
	}

	fun refresh(refreshToken: String): JwtToken =
		jwtService.generateAccessTokenFromRefreshToken(refreshToken)
}
