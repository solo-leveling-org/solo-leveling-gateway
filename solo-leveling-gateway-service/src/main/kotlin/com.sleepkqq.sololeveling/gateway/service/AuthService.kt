package com.sleepkqq.sololeveling.gateway.service

import com.sleepkqq.sololeveling.gateway.localization.LocalizationException
import com.sleepkqq.sololeveling.gateway.localization.LocalizationMessage
import com.sleepkqq.sololeveling.gateway.model.JwtResponse
import com.sleepkqq.sololeveling.gateway.model.TgAuthData
import org.springframework.stereotype.Service

@Service
class AuthService(
	private val jwtService: JwtService,
	private val tgHashService: TgHashService
) {

	fun authenticate(tgAuthData: TgAuthData): JwtResponse {
		if (!tgHashService.checkHash(tgAuthData.initData, tgAuthData.tgWebAppData.hash)) {
			throw LocalizationException(LocalizationMessage.ERROR_AUTH_HASH)
		}

		return jwtService.generateToken(tgAuthData.tgWebAppData.user)
	}
}
