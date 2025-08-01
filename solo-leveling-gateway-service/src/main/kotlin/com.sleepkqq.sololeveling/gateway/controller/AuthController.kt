package com.sleepkqq.sololeveling.gateway.controller

import com.sleepkqq.sololeveling.gateway.api.AuthApi
import com.sleepkqq.sololeveling.gateway.dto.RestLoginResponse
import com.sleepkqq.sololeveling.gateway.dto.RestRefreshRequest
import com.sleepkqq.sololeveling.gateway.dto.RestRefreshResponse
import com.sleepkqq.sololeveling.gateway.dto.RestTgAuthData
import com.sleepkqq.sololeveling.gateway.grpc.client.UserGrpcApi
import com.sleepkqq.sololeveling.gateway.mapper.ProtoMapper
import com.sleepkqq.sololeveling.gateway.model.UserData
import com.sleepkqq.sololeveling.gateway.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class AuthController(
	private val authService: AuthService,
	private val userGrpcApi: UserGrpcApi,
	private val protoMapper: ProtoMapper
) : AuthApi {

	override fun login(tgAuthData: @Valid RestTgAuthData): ResponseEntity<RestLoginResponse> {
		val userData = UserData.fromTgUser(tgAuthData.tgWebAppData.user)
		userGrpcApi.authUser(protoMapper.map(userData))
		return ResponseEntity.ok(authService.login(tgAuthData))
	}

	override fun refresh(refreshRequest: @Valid RestRefreshRequest): ResponseEntity<RestRefreshResponse> {
		val accessToken = authService.refresh(refreshRequest.refreshToken)
		return ResponseEntity.ok(RestRefreshResponse(accessToken))
	}
}
