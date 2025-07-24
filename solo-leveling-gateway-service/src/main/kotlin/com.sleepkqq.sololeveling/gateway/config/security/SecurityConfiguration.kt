package com.sleepkqq.sololeveling.gateway.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
	private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

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
		.authorizeHttpRequests {
			it.requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/api-docs/**").permitAll()
				.anyRequest().authenticated()
		}
		.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
		.build()
}