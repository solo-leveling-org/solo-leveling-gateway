package com.soloist.gateway.config.security

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository

@Configuration
class SecurityContextConfig {

	@PostConstruct
	fun setSecurityContextHolderStrategy() {
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
	}

	@Bean
	fun securityContextRepository() = RequestAttributeSecurityContextRepository()
}