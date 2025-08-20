package com.sleepkqq.sololeveling.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.nativex.hint.TypeAccess
import org.springframework.nativex.hint.TypeHint
import org.springframework.retry.annotation.EnableRetry

@TypeHint(
	typeNames = [
		"io.jsonwebtoken.impl.security.KeysBridge",
		"io.jsonwebtoken.impl.DefaultJwtParser",
		"io.jsonwebtoken.impl.DefaultJwtBuilder"
	],
	access = [TypeAccess.DECLARED_METHODS, TypeAccess.DECLARED_FIELDS]
)
@EnableRetry
@SpringBootApplication
class Application : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
