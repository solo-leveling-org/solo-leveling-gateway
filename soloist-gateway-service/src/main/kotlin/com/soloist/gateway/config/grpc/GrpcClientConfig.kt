package com.soloist.gateway.config.grpc

import com.soloist.gateway.config.properties.GrpcPlayerServiceProperties
import com.soloist.proto.balance.BalanceServiceGrpcKt
import com.soloist.proto.config.DefaultGrpcClientConfig
import com.soloist.proto.config.interceptor.UserClientInterceptor
import com.soloist.proto.player.PlayerServiceGrpcKt
import com.soloist.proto.task.TaskServiceGrpcKt
import com.soloist.proto.user.UserServiceGrpcKt
import io.grpc.ManagedChannel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcClientConfig(
	properties: GrpcPlayerServiceProperties
) : DefaultGrpcClientConfig(properties) {

	private val interceptor = UserClientInterceptor()

	@Bean
	fun playerManagedChannel(): ManagedChannel = createManagedChannel()

	@Bean
	fun userServiceBlockingStub(channel: ManagedChannel): UserServiceGrpcKt.UserServiceCoroutineStub =
		UserServiceGrpcKt.UserServiceCoroutineStub(channel).withInterceptors(interceptor)

	@Bean
	fun playerServiceBlockingStub(channel: ManagedChannel): PlayerServiceGrpcKt.PlayerServiceCoroutineStub =
		PlayerServiceGrpcKt.PlayerServiceCoroutineStub(channel).withInterceptors(interceptor)

	@Bean
	fun taskServiceCoroutineStub(channel: ManagedChannel): TaskServiceGrpcKt.TaskServiceCoroutineStub =
		TaskServiceGrpcKt.TaskServiceCoroutineStub(channel).withInterceptors(interceptor)

	@Bean
	fun balanceServiceCoroutineStub(channel: ManagedChannel): BalanceServiceGrpcKt.BalanceServiceCoroutineStub =
		BalanceServiceGrpcKt.BalanceServiceCoroutineStub(channel).withInterceptors(interceptor)
}