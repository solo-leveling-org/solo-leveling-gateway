package com.sleepkqq.sololeveling.gateway.grpc.client

import com.google.protobuf.Empty
import com.sleepkqq.sololeveling.proto.user.AuthUserRequest
import com.sleepkqq.sololeveling.proto.user.GetUserAdditionalInfoResponse
import com.sleepkqq.sololeveling.proto.user.GetUserLeaderboardRequest
import com.sleepkqq.sololeveling.proto.user.GetUserLeaderboardResponse
import com.sleepkqq.sololeveling.proto.user.GetUserRequest
import com.sleepkqq.sololeveling.proto.user.GetUsersLeaderboardRequest
import com.sleepkqq.sololeveling.proto.user.GetUsersLeaderboardResponse
import com.sleepkqq.sololeveling.proto.user.UpdateUserLocaleRequest
import com.sleepkqq.sololeveling.proto.user.UserInput
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub
import com.sleepkqq.sololeveling.proto.user.UserView
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class UserApi(
	private val userStub: UserServiceBlockingStub
) {

	fun getUser(userId: Long): UserView = userStub.getUser(
		GetUserRequest.newBuilder().setUserId(userId).build()
	)
		.user

	fun authUser(userInput: UserInput): Empty =
		userStub.authUser(AuthUserRequest.newBuilder().setUser(userInput).build())

	fun updateUserLocale(locale: Locale): Empty =
		userStub.updateUserLocale(UpdateUserLocaleRequest.newBuilder().setTag(locale.language).build())

	fun getUserAdditionalInfo(): GetUserAdditionalInfoResponse =
		userStub.getUserAdditionalInfo(Empty.newBuilder().build())

	fun getUsersLeaderboard(request: GetUsersLeaderboardRequest): GetUsersLeaderboardResponse =
		userStub.getUsersLeaderboard(request)

	fun getUserLeaderboard(request: GetUserLeaderboardRequest): GetUserLeaderboardResponse =
		userStub.getUserLeaderboard(request)
}
