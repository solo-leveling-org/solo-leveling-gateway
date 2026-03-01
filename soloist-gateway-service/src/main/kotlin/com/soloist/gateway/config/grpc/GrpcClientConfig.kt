package com.soloist.gateway.config.grpc

import com.soloist.gateway.config.properties.GrpcPlayerServiceProperties
import com.soloist.proto.balance.BalanceServiceGrpc
import com.soloist.proto.config.DefaultGrpcClientConfig
import com.soloist.proto.config.interceptor.UserClientInterceptor
import com.soloist.proto.player.PlayerServiceGrpc
import com.soloist.proto.task.TaskServiceGrpc
import com.soloist.proto.user.UserServiceGrpc
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
	fun userServiceBlockingStub(channel: ManagedChannel): UserServiceGrpc.UserServiceBlockingStub =
		UserServiceGrpc.newBlockingStub(channel).withInterceptors(interceptor)

	@Bean
	fun playerServiceBlockingStub(channel: ManagedChannel): PlayerServiceGrpc.PlayerServiceBlockingStub =
		PlayerServiceGrpc.newBlockingStub(channel).withInterceptors(interceptor)

	@Bean
	fun taskServiceBlockingStub(channel: ManagedChannel): TaskServiceGrpc.TaskServiceBlockingStub =
		TaskServiceGrpc.newBlockingStub(channel).withInterceptors(interceptor)

	@Bean
	fun balanceServiceBlockingStub(channel: ManagedChannel): BalanceServiceGrpc.BalanceServiceBlockingStub =
		BalanceServiceGrpc.newBlockingStub(channel).withInterceptors(interceptor)
}