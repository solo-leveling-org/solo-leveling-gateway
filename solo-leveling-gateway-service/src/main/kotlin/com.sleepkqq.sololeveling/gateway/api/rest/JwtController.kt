package com.sleepkqq.sololeveling.gateway.api.rest

import com.sleepkqq.sololeveling.gateway.api.JwtApi
import com.sleepkqq.sololeveling.gateway.api.grpc.UserApi
import com.sleepkqq.sololeveling.gateway.dto.JwtResponse
import com.sleepkqq.sololeveling.gateway.dto.JwtToken
import com.sleepkqq.sololeveling.gateway.dto.RefreshRequest
import com.sleepkqq.sololeveling.gateway.dto.TgAuthData
import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.gateway.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class JwtController(
	private val authService: AuthService,
	private val userApi: UserApi
) : JwtApi {

	override fun login(tgAuthData: @Valid TgAuthData): ResponseEntity<JwtResponse> {
		userApi.authUser(UserData.fromTgUser(tgAuthData.tgWebAppData.user))
		return ResponseEntity.ok(authService.login(tgAuthData))
	}

	override fun refresh(refreshRequest: @Valid RefreshRequest): ResponseEntity<JwtToken> =
		ResponseEntity.ok(authService.refresh(refreshRequest.refreshToken))
}
