package com.sleepkqq.sololeveling.gateway.config

import com.sleepkqq.sololeveling.avro.config.DefaultKafkaConfig
import com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory

@EnableKafka
@Configuration
@Suppress("unused")
class KafkaConfig(
	@Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
	@Value("\${spring.kafka.properties.schema.registry.url}") schemaRegistryUrl: String
) : DefaultKafkaConfig(bootstrapServers, schemaRegistryUrl) {

	@Bean
	fun consumerFactoryReceiveNotificationEvent(): ConsumerFactory<String, ReceiveNotificationEvent> {
		return createConsumerFactory(KafkaGroupIds.UI_GROUP_ID)
	}

	@Bean
	fun kafkaListenerContainerFactory(
		consumerFactory: ConsumerFactory<String, ReceiveNotificationEvent>
	): ConcurrentKafkaListenerContainerFactory<String, ReceiveNotificationEvent> =
		createKafkaListenerContainerFactory(consumerFactory)
}
