package com.soloist.gateway.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository

@Configuration
class SecurityContextConfig {

	@Bean
	fun securityContextRepository() = RequestAttributeSecurityContextRepository()
}