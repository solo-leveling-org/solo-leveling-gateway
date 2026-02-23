package com.soloist.gateway.grpc.client

import com.google.protobuf.Empty
import com.soloist.proto.user.AuthUserRequest
import com.soloist.proto.user.GetUserAdditionalInfoResponse
import com.soloist.proto.user.GetUserLeaderboardRequest
import com.soloist.proto.user.GetUserLeaderboardResponse
import com.soloist.proto.user.GetUserRequest
import com.soloist.proto.user.GetUsersLeaderboardRequest
import com.soloist.proto.user.GetUsersLeaderboardResponse
import com.soloist.proto.user.UpdateUserLocaleRequest
import com.soloist.proto.user.UserInput
import com.soloist.proto.user.UserServiceGrpc.UserServiceBlockingStub
import com.soloist.proto.user.UserView
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
