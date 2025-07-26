package com.sleepkqq.sololeveling.gateway.config.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Suppress("unused")
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
					allowedMethods = listOf(
						HttpMethod.GET.name(),
						HttpMethod.POST.name(),
						HttpMethod.PUT.name(),
						HttpMethod.DELETE.name(),
						HttpMethod.OPTIONS.name()
					)
					allowedHeaders = listOf("*")
					allowCredentials = true
				}
			}
		}
		.authorizeHttpRequests {
			it.requestMatchers("/actuator/**").permitAll()
				.requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
				.anyRequest().authenticated()
		}
		.exceptionHandling {
			it.authenticationEntryPoint { request, response, authException ->
				response.status = HttpServletResponse.SC_UNAUTHORIZED
			}
		}
		.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
		.build()
}
