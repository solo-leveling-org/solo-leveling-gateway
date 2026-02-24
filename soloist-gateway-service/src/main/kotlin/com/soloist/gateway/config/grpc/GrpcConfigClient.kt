package com.soloist.gateway.config.grpc

import com.soloist.proto.config.DefaultGrpcClientConfig
import com.soloist.proto.config.interceptor.UserClientInterceptor
import com.soloist.proto.player.PlayerServiceGrpc
import com.soloist.proto.user.UserServiceGrpc
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannel
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GrpcPlayerServiceProperties::class)
class GrpcConfigClient(
	properties: GrpcPlayerServiceProperties
) : DefaultGrpcClientConfig(properties) {

	@Bean
	fun playerManagedChannel(): ManagedChannel = createManagedChannel()

	@Bean
	fun userClientInterceptor(): ClientInterceptor = UserClientInterceptor()

	@Bean
	fun userServiceBlockingStub(): UserServiceGrpc.UserServiceBlockingStub =
		UserServiceGrpc.newBlockingStub(playerManagedChannel())
			.withInterceptors(userClientInterceptor())

	@Bean
	fun playerServiceBlockingStub(): PlayerServiceGrpc.PlayerServiceBlockingStub =
		PlayerServiceGrpc.newBlockingStub(playerManagedChannel())
			.withInterceptors(userClientInterceptor())
}
