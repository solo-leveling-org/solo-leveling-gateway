package com.sleepkqq.sololeveling.gateway.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

	@Bean
	@Throws(Exception::class)
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
		.csrf { it.disable() }
		.cors {
			it.configurationSource {
				CorsConfiguration().apply {
					allowedOriginPatterns = listOf("*")
					allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
					allowedHeaders = listOf("*")
					allowCredentials = true
				}
			}
		}
		.authorizeHttpRequests { it.anyRequest().permitAll() }
		.build()
}