package com.soloist.gateway.client

import com.soloist.gateway.graphql.types.*
import com.soloist.gateway.graphql.types.LeaderboardUser
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.gateway.model.UserData
import com.soloist.proto.user.*
import org.springframework.stereotype.Service

@Service
class UserClient(
	private val userStub: UserServiceGrpcKt.UserServiceCoroutineStub,
	private val protoMapper: ProtoMapper
) {

	suspend fun authUser(userData: UserData) {
		val request = AuthUserRequest.newBuilder().setUser(protoMapper.map(userData)).build()
		userStub.authUser(request)
	}

	suspend fun getUser(userId: Long): User {
		val request = GetUserRequest.newBuilder().setUserId(userId).build()
		val response = userStub.getUser(request)
		return protoMapper.map(response.user)
	}

	suspend fun updateUserLocale(locale: UserLocaleInput) {
		val request = UpdateUserLocaleRequest.newBuilder().setLocale(protoMapper.map(locale)).build()
		userStub.updateUserLocale(request)
	}

	suspend fun getUsersLeaderboard(
		paging: PagingInput,
		filter: LeaderboardFilterInput
	): UsersLeaderboardResult {
		val request = protoMapper.map(paging, filter)
		val response = userStub.getUsersLeaderboard(request)
		return protoMapper.map(response)
	}

	suspend fun getUserLeaderboard(filter: LeaderboardFilterInput): LeaderboardUser {
		val request = protoMapper.map(filter)
		val response = userStub.getUserLeaderboard(request)
		return protoMapper.map(response.user)
	}
}
