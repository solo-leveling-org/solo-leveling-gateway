package com.soloist.gateway.controller

import com.soloist.gateway.api.AuthRestApi
import com.soloist.gateway.dto.RestLoginResponse
import com.soloist.gateway.dto.RestRefreshRequest
import com.soloist.gateway.dto.RestRefreshResponse
import com.soloist.gateway.dto.RestTgAuthData
import com.soloist.gateway.grpc.client.UserApi
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.gateway.model.UserData
import com.soloist.gateway.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
	private val authService: AuthService,
	private val userApi: UserApi,
	private val protoMapper: ProtoMapper
) : AuthRestApi {

	override fun login(tgAuthData: @Valid RestTgAuthData): ResponseEntity<RestLoginResponse> {
		val userData = UserData.fromTgUser(tgAuthData.tgWebAppData.user)
		val response = authService.login(tgAuthData)
		userApi.authUser(protoMapper.map(userData))
		return ResponseEntity.ok(response)
	}

	override fun refresh(refreshRequest: @Valid RestRefreshRequest): ResponseEntity<RestRefreshResponse> {
		val accessToken = authService.refresh(refreshRequest.refreshToken)
		return ResponseEntity.ok(RestRefreshResponse(accessToken))
	}
}
