package com.soloist.gateway.config.security

import com.soloist.gateway.config.properties.CorsProperties
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
	private val jwtAuthenticationFilter: JwtAuthenticationFilter,
	private val corsProperties: CorsProperties,
	private val securityContextRepository: RequestAttributeSecurityContextRepository
) {

	@Bean
	@Throws(Exception::class)
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
		.csrf { it.disable() }
		.cors {
			it.configurationSource {
				CorsConfiguration().apply {
					allowedOriginPatterns = corsProperties.hosts
					allowedMethods = corsProperties.methods
					allowedHeaders = listOf("*")
					allowCredentials = true
				}
			}
		}
		.authorizeHttpRequests {

			it
				// metrics
				.requestMatchers("/actuator/**").permitAll()
				// auth
				.requestMatchers("/api/auth/**").permitAll()
				// swagger
				.requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
				// websocket
				.requestMatchers("/ws/**", "/app/**", "/user/**", "/topic/**").permitAll()
				// other
				.anyRequest().authenticated()
		}
		.exceptionHandling {
			it.authenticationEntryPoint { _, response, _ ->
				response.status = HttpServletResponse.SC_UNAUTHORIZED
			}
		}
		.securityContext { it.securityContextRepository(securityContextRepository) }
		.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
		.build()
}
