package com.sleepkqq.sololeveling.gateway.controller

import com.sleepkqq.sololeveling.gateway.api.UserApi
import com.sleepkqq.sololeveling.gateway.dto.RestGetUserResponse
import com.sleepkqq.sololeveling.gateway.dto.RestUpdateUserLocaleRequest
import com.sleepkqq.sololeveling.gateway.dto.RestUserLocaleResponse
import com.sleepkqq.sololeveling.gateway.grpc.client.UserGrpcApi
import com.sleepkqq.sololeveling.gateway.mapper.ProtoMapper
import com.sleepkqq.sololeveling.gateway.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.Locale

@Suppress("unused")
@RestController
class UserController(
	private val authService: AuthService,
	private val userGrpcApi: UserGrpcApi,
	private val protoMapper: ProtoMapper
) : UserApi {

	override fun getCurrentUser(): ResponseEntity<RestGetUserResponse> {
		val currentUser = authService.getCurrentUser()
		val user = userGrpcApi.getUser(currentUser.id)

		val response = RestGetUserResponse()
			.user(protoMapper.map(user))
		return ResponseEntity.ok(response)
	}

	override fun getUser(userId: Long): ResponseEntity<RestGetUserResponse> {
		val user = userGrpcApi.getUser(userId)

		val response = RestGetUserResponse()
			.user(protoMapper.map(user))
		return ResponseEntity.ok(response)
	}

	override fun getUserLocale(): ResponseEntity<RestUserLocaleResponse> {
		val currentUser = authService.getCurrentUser()

		val response = userGrpcApi.getUserLocale(currentUser.id)
		return ResponseEntity.ok(protoMapper.map(response))
	}

	override fun updateUserLocale(request: @Valid RestUpdateUserLocaleRequest):
			ResponseEntity<RestUserLocaleResponse> {

		val currentUser = authService.getCurrentUser()

		val locale = Locale.forLanguageTag(request.locale)
		val response = userGrpcApi.updateUserLocale(currentUser.id, locale)
		return ResponseEntity.ok(protoMapper.map(response))
	}
}
