package com.sleepkqq.sololeveling.gateway.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sleepkqq.sololeveling.gateway.mapper.StringToLocalDateTimeDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class ObjectMapperConfig {

	@Bean
	fun objectMapper(): ObjectMapper = jacksonObjectMapper()
		.registerModule(
			JavaTimeModule()
				.addDeserializer(LocalDateTime::class.java, StringToLocalDateTimeDeserializer())
		)
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}
