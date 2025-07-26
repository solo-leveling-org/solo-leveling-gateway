package com.sleepkqq.sololeveling.gateway.config

import com.sleepkqq.sololeveling.gateway.config.properties.GrpcPlayerServiceProperties
import com.sleepkqq.sololeveling.proto.config.DefaultGrpcClientConfig
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub
import io.grpc.ManagedChannel
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
@EnableConfigurationProperties(GrpcPlayerServiceProperties::class)
class GrpcConfigClient(
	private val properties: GrpcPlayerServiceProperties
) : DefaultGrpcClientConfig() {

	@Bean
	fun playerManagedChannel(): ManagedChannel = createManagedChannel(properties)

	@Bean
	fun userServiceBlockingStub(
		playerManagedChannel: ManagedChannel
	): UserServiceBlockingStub = UserServiceGrpc.newBlockingStub(playerManagedChannel)

	@Bean
	fun playerServiceBlockingStub(
		playerManagedChannel: ManagedChannel
	): PlayerServiceBlockingStub = PlayerServiceGrpc.newBlockingStub(playerManagedChannel)
}
