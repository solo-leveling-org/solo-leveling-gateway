package com.sleepkqq.sololeveling.gateway.controller

import com.sleepkqq.sololeveling.gateway.api.AuthRestApi
import com.sleepkqq.sololeveling.gateway.dto.RestLoginResponse
import com.sleepkqq.sololeveling.gateway.dto.RestRefreshRequest
import com.sleepkqq.sololeveling.gateway.dto.RestRefreshResponse
import com.sleepkqq.sololeveling.gateway.dto.RestTgAuthData
import com.sleepkqq.sololeveling.gateway.grpc.client.UserApi
import com.sleepkqq.sololeveling.gateway.mapper.ProtoMapper
import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.gateway.service.auth.AuthService
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
