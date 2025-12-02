package com.sleepkqq.sololeveling.gateway.controller

import com.sleepkqq.sololeveling.gateway.api.UserRestApi
import com.sleepkqq.sololeveling.gateway.dto.RestGetUserResponse
import com.sleepkqq.sololeveling.gateway.dto.RestGetUsersLeaderboardRequest
import com.sleepkqq.sololeveling.gateway.dto.RestGetUsersLeaderboardResponse
import com.sleepkqq.sololeveling.gateway.dto.RestLeaderboardType
import com.sleepkqq.sololeveling.gateway.dto.RestUpdateUserLocaleRequest
import com.sleepkqq.sololeveling.gateway.dto.RestUserLocaleResponse
import com.sleepkqq.sololeveling.gateway.grpc.client.UserApi
import com.sleepkqq.sololeveling.gateway.mapper.ProtoMapper
import com.sleepkqq.sololeveling.gateway.service.auth.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.Locale

@RestController
class UserController(
	private val authService: AuthService,
	private val userApi: UserApi,
	private val protoMapper: ProtoMapper
) : UserRestApi {

	override fun getCurrentUser(): ResponseEntity<RestGetUserResponse> {
		val currentUser = authService.getCurrentUser()
		val user = userApi.getUser(currentUser.id)

		val response = RestGetUserResponse()
			.user(protoMapper.map(user))
		return ResponseEntity.ok(response)
	}

	override fun getUser(userId: Long): ResponseEntity<RestGetUserResponse> {
		val user = userApi.getUser(userId)

		val response = RestGetUserResponse()
			.user(protoMapper.map(user))
		return ResponseEntity.ok(response)
	}

	override fun getUserLocale(): ResponseEntity<RestUserLocaleResponse> {
		val grpcResponse = userApi.getUserLocale()

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun updateUserLocale(request: @Valid RestUpdateUserLocaleRequest):
			ResponseEntity<RestUserLocaleResponse> {

		val locale = Locale.forLanguageTag(request.locale)
		val grpcResponse = userApi.updateUserLocale(locale)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getUsersLeaderboard(
		type: RestLeaderboardType,
		request: @Valid RestGetUsersLeaderboardRequest,
		page: @Min(value = 0) @Valid Int,
		pageSize: @Min(value = 1) @Max(value = 100) @Valid Int
	): ResponseEntity<RestGetUsersLeaderboardResponse> {

		val grpcResponse = userApi.getUsersLeaderboard(protoMapper.map(type, request.range, page, pageSize))

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}
}
