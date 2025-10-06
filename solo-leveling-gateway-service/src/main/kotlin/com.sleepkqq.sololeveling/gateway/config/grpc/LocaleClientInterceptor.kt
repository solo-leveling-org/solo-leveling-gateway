package com.sleepkqq.sololeveling.gateway.config.grpc

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class LocaleClientInterceptor : ClientInterceptor {

	private companion object {
		const val LOCALE_METADATA_KEY = "x-locale"
	}

	private val log = LoggerFactory.getLogger(javaClass)

	override fun <ReqT, RespT> interceptCall(
		method: MethodDescriptor<ReqT, RespT>,
		callOptions: CallOptions,
		next: Channel
	): ClientCall<ReqT, RespT> {

		return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
			next.newCall(method, callOptions)
		) {
			override fun start(responseListener: Listener<RespT>, headers: Metadata) {
				val currentLocale = LocaleContextHolder.getLocale().language
				headers.put(
					Metadata.Key.of(LOCALE_METADATA_KEY, Metadata.ASCII_STRING_MARSHALLER),
					currentLocale
				)

				log.info("Current locale: {}", currentLocale.toString())

				super.start(responseListener, headers)
			}
		}
	}
}