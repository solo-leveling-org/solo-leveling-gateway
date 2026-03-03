package com.soloist.gateway.config.web

import com.soloist.config.interceptor.UserContextHolder
import org.slf4j.LoggerFactory
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TimingGraphQlInterceptor : WebGraphQlInterceptor {

	private val log = LoggerFactory.getLogger(javaClass)

	override fun intercept(
		request: WebGraphQlRequest,
		chain: WebGraphQlInterceptor.Chain
	): Mono<WebGraphQlResponse> {
		val start = System.currentTimeMillis()
		val userId = UserContextHolder.getUserId()?.toString() ?: "anonymous"

		return chain.next(request).doFinally {
			val duration = System.currentTimeMillis() - start
			log.info(">> GraphQL executed by '{}' in {} ms", userId, duration)
		}
	}
}