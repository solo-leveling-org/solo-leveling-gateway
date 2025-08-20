package com.sleepkqq.sololeveling.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
class Application : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
