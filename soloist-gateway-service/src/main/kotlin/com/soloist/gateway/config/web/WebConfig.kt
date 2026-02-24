package com.soloist.gateway.config.web

import com.soloist.rest.config.interceptor.UserRestInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
	private val userRestInterceptor: UserRestInterceptor
) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(userRestInterceptor)
	}
}
