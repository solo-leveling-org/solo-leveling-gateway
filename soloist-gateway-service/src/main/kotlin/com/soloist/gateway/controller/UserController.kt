package com.soloist.gateway.controller

import com.soloist.gateway.api.UserRestApi
import com.soloist.gateway.dto.RestGetUserLeaderboardResponse
import com.soloist.gateway.dto.RestGetUserResponse
import com.soloist.gateway.dto.RestGetUsersLeaderboardRequest
import com.soloist.gateway.dto.RestGetUsersLeaderboardResponse
import com.soloist.gateway.dto.RestLeaderboardType
import com.soloist.gateway.dto.RestUpdateUserLocaleRequest
import com.soloist.gateway.dto.RestUserAdditionalInfoResponse
import com.soloist.gateway.grpc.client.UserApi
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.gateway.service.auth.AuthService
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

	override fun getUserAdditionalInfo(): ResponseEntity<RestUserAdditionalInfoResponse> {
		val grpcResponse = userApi.getUserAdditionalInfo()

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun updateUserLocale(request: @Valid RestUpdateUserLocaleRequest): ResponseEntity<Void> {

		val locale = Locale.forLanguageTag(request.locale)
		userApi.updateUserLocale(locale)

		return ResponseEntity.noContent().build()
	}

	override fun getUsersLeaderboard(
		type: RestLeaderboardType,
		request: @Valid RestGetUsersLeaderboardRequest,
		page: @Min(value = 0) @Valid Int,
		pageSize: @Min(value = 1) @Max(value = 100) @Valid Int
	): ResponseEntity<RestGetUsersLeaderboardResponse> {

		val grpcResponse = userApi.getUsersLeaderboard(
			protoMapper.map(type, request.range, page, pageSize)
		)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}

	override fun getUserLeaderboard(
		type: RestLeaderboardType,
		request: @Valid RestGetUsersLeaderboardRequest
	): ResponseEntity<RestGetUserLeaderboardResponse> {

		val grpcResponse = userApi.getUserLeaderboard(
			protoMapper.map(type, request.range)
		)

		return ResponseEntity.ok(protoMapper.map(grpcResponse))
	}
}
