package com.soloist.gateway.config.grpc

import com.soloist.proto.balance.BalanceServiceGrpcKt.BalanceServiceCoroutineStub
import com.soloist.proto.config.GrpcChannelFactory
import com.soloist.proto.media.MediaServiceGrpcKt.MediaServiceCoroutineStub
import com.soloist.proto.player.PlayerServiceGrpcKt.PlayerServiceCoroutineStub
import com.soloist.proto.task.TaskServiceGrpcKt.TaskServiceCoroutineStub
import com.soloist.proto.user.UserServiceGrpcKt.UserServiceCoroutineStub
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcStubConfig(private val grpc: GrpcChannelFactory) {

	@Bean
	fun userStub(): UserServiceCoroutineStub =
		grpc.stub("player") { UserServiceCoroutineStub(it) }

	@Bean
	fun playerStub(): PlayerServiceCoroutineStub =
		grpc.stub("player") { PlayerServiceCoroutineStub(it) }

	@Bean
	fun taskStub(): TaskServiceCoroutineStub =
		grpc.stub("player") { TaskServiceCoroutineStub(it) }

	@Bean
	fun balanceStub(): BalanceServiceCoroutineStub =
		grpc.stub("player") { BalanceServiceCoroutineStub(it) }

	@Bean
	fun mediaStub(): MediaServiceCoroutineStub =
		grpc.stub("media") { MediaServiceCoroutineStub(it) }
}
