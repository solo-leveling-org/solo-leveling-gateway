package com.sleepkqq.sololeveling.ui.config

import com.sleepkqq.sololeveling.proto.config.GrpcServiceProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.grpc.services.player")
data class GrpcPlayerServiceProperties(
	override val host: String,
	override val port: Int
) : GrpcServiceProperties
