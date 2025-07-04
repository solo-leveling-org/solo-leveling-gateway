package com.sleepkqq.sololeveling.ui.config

import com.sleepkqq.sololeveling.proto.config.DefaultGrpcClientConfig
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc
import com.sleepkqq.sololeveling.proto.player.PlayerServiceGrpc.PlayerServiceBlockingStub
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc
import com.sleepkqq.sololeveling.proto.user.UserServiceGrpc.UserServiceBlockingStub
import io.grpc.ManagedChannel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfigClient(
	@Value("\${app.host}") host: String
) : DefaultGrpcClientConfig(host) {

	@Bean
	fun playerManagedChannel(@Value("\${app.grpc.services.player.port}") port: Int): ManagedChannel =
		createManagedChannel(port)

	@Bean
	fun userServiceBlockingStub(
		playerManagedChannel: ManagedChannel
	): UserServiceBlockingStub = UserServiceGrpc.newBlockingStub(playerManagedChannel)

	@Bean
	fun playerServiceBlockingStub(
		playerManagedChannel: ManagedChannel
	): PlayerServiceBlockingStub = PlayerServiceGrpc.newBlockingStub(playerManagedChannel)
}
