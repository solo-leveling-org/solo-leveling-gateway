package com.sleepkqq.sololeveling.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
class Application : SpringBootServletInitializer() {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(Application::class.java, *args)
		}
	}
}
