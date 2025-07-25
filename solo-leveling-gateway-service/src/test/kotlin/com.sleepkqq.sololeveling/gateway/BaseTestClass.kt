package com.sleepkqq.sololeveling.gateway

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.ConfluentKafkaContainer

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseTestClass {

	companion object {
		@JvmStatic
		@Container
		val kafkaContainer = ConfluentKafkaContainer("confluentinc/cp-kafka:latest")
	}
}
