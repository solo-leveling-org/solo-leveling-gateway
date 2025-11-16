package com.sleepkqq.sololeveling.gateway.grpc.client

import com.google.protobuf.Empty
import com.sleepkqq.sololeveling.proto.user.AuthUserRequest
import com.sleepkqq.sololeveling.proto.user.GetUserLocaleRequest
import com.sleepkqq.sololeveling.proto.user.GetUserRequest
import com.sleepkqq.sololeveling.proto.user.UpdateUserLocaleRequest
import com.sleepkqq.sololeveling.proto.user.UserInput
import com.sleepkqq.sololeveling.proto.user.UserLocaleResponse
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

	fun updateUserLocale(userId: Long, locale: Locale): UserLocaleResponse = userStub.updateUserLocale(
		UpdateUserLocaleRequest.newBuilder()
			.setUserId(userId)
			.setLocale(locale.language)
			.build()
	)

	fun getUserLocale(userId: Long): UserLocaleResponse =
		userStub.getUserLocale(GetUserLocaleRequest.newBuilder().setUserId(userId).build())
}
