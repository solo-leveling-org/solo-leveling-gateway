package com.sleepkqq.sololeveling.gateway.config.grpc

import com.sleepkqq.sololeveling.proto.config.DefaultGrpcClientConfig
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc
import io.grpc.ManagedChannel
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
@EnableConfigurationProperties(GrpcPlayerServiceProperties::class)
class GrpcConfigClient(
	private val properties: GrpcPlayerServiceProperties,
	private val localeClientInterceptor: LocaleClientInterceptor
) : DefaultGrpcClientConfig() {

	@Bean
	fun playerManagedChannel(): ManagedChannel = createManagedChannel(properties)

	@Bean
	fun userServiceBlockingStub(): UserServiceGrpc.UserServiceBlockingStub =
		UserServiceGrpc.newBlockingStub(playerManagedChannel())
			.withInterceptors(localeClientInterceptor)

	@Bean
	fun playerServiceBlockingStub(): PlayerServiceGrpc.PlayerServiceBlockingStub =
		PlayerServiceGrpc.newBlockingStub(playerManagedChannel())
			.withInterceptors(localeClientInterceptor)
}
