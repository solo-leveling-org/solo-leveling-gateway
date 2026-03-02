package com.soloist.gateway.client

import com.soloist.gateway.graphql.types.*
import com.soloist.gateway.graphql.types.LeaderboardUser
import com.soloist.gateway.mapper.ProtoMapper
import com.soloist.gateway.model.UserData
import com.soloist.proto.user.*
import org.springframework.stereotype.Service

@Service
class UserClient(
	private val userStub: UserServiceGrpc.UserServiceBlockingStub,
	private val protoMapper: ProtoMapper
) {

	fun authUser(userData: UserData) {
		val request = AuthUserRequest.newBuilder().setUser(protoMapper.map(userData)).build()
		userStub.authUser(request)
	}

	fun getUser(userId: Long): User {
		val request = GetUserRequest.newBuilder().setUserId(userId).build()
		val response = userStub.getUser(request)
		return protoMapper.map(response.user)
	}

	fun updateUserLocale(locale: UserLocaleInput) {
		val request = UpdateUserLocaleRequest.newBuilder().setLocale(protoMapper.map(locale)).build()
		userStub.updateUserLocale(request)
	}

	fun getUsersLeaderboard(
		paging: PagingInput,
		filter: LeaderboardFilterInput
	): UsersLeaderboardResult {
		val request = protoMapper.map(paging, filter)
		val response = userStub.getUsersLeaderboard(request)
		return protoMapper.map(response)
	}

	fun getUserLeaderboard(filter: LeaderboardFilterInput): LeaderboardUser {
		val request = protoMapper.map(filter)
		val response = userStub.getUserLeaderboard(request)
		return protoMapper.map(response.user)
	}
}
