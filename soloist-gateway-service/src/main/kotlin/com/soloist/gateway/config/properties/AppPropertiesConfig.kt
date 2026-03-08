package com.soloist.gateway.config.properties

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(
	CorsProperties::class,
	GrpcPlayerServiceProperties::class,
	JwtProperties::class,
	RabbitMqStompProperties::class
)
@Configuration
class AppPropertiesConfig
