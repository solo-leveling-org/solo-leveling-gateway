package com.soloist.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@ConfigurationPropertiesScan("com.soloist.gateway.config.properties")
@SpringBootApplication
class Application : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
