package com.sleepkqq.sololeveling.gateway.grpc.client

import com.sleepkqq.sololeveling.proto.user.AuthUserRequest
import com.sleepkqq.sololeveling.proto.user.GetUserRequest
import com.sleepkqq.sololeveling.proto.user.UserInput
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub
import com.sleepkqq.sololeveling.proto.user.UserView
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service
class UserGrpcApi(
	private val userStub: UserServiceBlockingStub
) {

	@Retryable
	fun getUser(userId: Long): UserView = userStub.getUser(
		GetUserRequest.newBuilder().setUserId(userId).build()
	)
		.user

	@Retryable
	fun authUser(userInput: UserInput) {
		userStub.authUser(AuthUserRequest.newBuilder().setUser(userInput).build())
	}
}
