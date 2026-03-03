package com.soloist.gateway.controller

import com.soloist.gateway.dto.auth.TgAuthData
import com.soloist.gateway.dto.auth.LoginResponse
import com.soloist.gateway.dto.auth.RefreshRequest
import com.soloist.gateway.dto.auth.RefreshResponse
import com.soloist.gateway.client.UserClient
import com.soloist.gateway.model.UserData
import com.soloist.gateway.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
	private val authService: AuthService,
	private val userClient: UserClient
) {

	@PostMapping("/login", version = "1")
	suspend fun login(@RequestBody @Valid tgAuthData: TgAuthData): ResponseEntity<LoginResponse> {
		val userData = UserData.fromTgUser(tgAuthData.tgWebAppData.user)
		val response = authService.login(tgAuthData)
		userClient.authUser(userData)
		return ResponseEntity.ok(response)
	}

	@PostMapping("/refresh", version = "1")
	suspend fun refresh(@RequestBody @Valid refreshRequest: RefreshRequest): ResponseEntity<RefreshResponse> {
		val accessToken = authService.refresh(refreshRequest.refreshToken)
		return ResponseEntity.ok(RefreshResponse(accessToken))
	}
}
