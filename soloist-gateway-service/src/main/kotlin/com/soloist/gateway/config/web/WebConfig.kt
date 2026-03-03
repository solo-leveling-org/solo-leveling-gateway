package com.soloist.gateway.config.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.soloist.gateway.mapper.UnixTimestampToOffsetDateTimeDeserializer
import graphql.scalars.ExtendedScalars
import io.micrometer.context.ContextRegistry
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import reactor.core.publisher.Hooks
import java.time.OffsetDateTime
import java.util.*

@Configuration
class WebConfig : WebMvcConfigurer {

	private companion object {
		const val API_VERSION_HEADER = "API-Version"
	}

	override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
		configurer.useRequestHeader(API_VERSION_HEADER)
	}

	@PostConstruct
	fun registerContextAccessors() {
		ContextRegistry.getInstance()
			.registerThreadLocalAccessor(UserContextAccessor())
			.registerThreadLocalAccessor(LocaleContextAccessor())

		Hooks.enableAutomaticContextPropagation()
	}

	@Bean
	fun objectMapper(): ObjectMapper = jacksonObjectMapper()
		.registerModule(
			JavaTimeModule()
				.addDeserializer(OffsetDateTime::class.java, UnixTimestampToOffsetDateTimeDeserializer())
		)
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

	@Bean
	fun localeResolver(): LocaleResolver = AcceptHeaderLocaleResolver().apply {
		supportedLocales = listOf(
			Locale.ENGLISH,
			Locale.forLanguageTag("ru")
		)
		setDefaultLocale(Locale.ENGLISH)
	}

	@Bean
	fun runtimeWiringConfigurer(): RuntimeWiringConfigurer = RuntimeWiringConfigurer {
		it.scalar(ExtendedScalars.GraphQLLong)
			.scalar(ExtendedScalars.GraphQLBigDecimal)
			.scalar(ExtendedScalars.DateTime)
			.scalar(ExtendedScalars.UUID)
			.scalar(ExtendedScalars.Date)
	}
}