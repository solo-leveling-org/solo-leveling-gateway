package com.soloist.gateway.config.rabbitmq

import com.soloist.event.RoutingKeys
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig {

	companion object {
		const val NOTIFICATION_QUEUE = "ui-notifications"
	}

	@Bean
	fun notificationQueue(): Queue = Queue(NOTIFICATION_QUEUE, true)

	@Bean
	fun notificationExchange(): TopicExchange = TopicExchange(RoutingKeys.EXCHANGE)

	@Bean
	fun notificationBinding(queue: Queue, exchange: TopicExchange): Binding =
		BindingBuilder.bind(queue).to(exchange).with("notification.ui.#")

	@Bean
	fun jsonMessageConverter(): JacksonJsonMessageConverter =
		JacksonJsonMessageConverter()

	@Bean
	fun rabbitTemplate(connectionFactory: ConnectionFactory, converter: JacksonJsonMessageConverter): RabbitTemplate =
		RabbitTemplate(connectionFactory).apply { messageConverter = converter }

	@Bean
	fun rabbitListenerContainerFactory(
		connectionFactory: ConnectionFactory,
		converter: JacksonJsonMessageConverter
	): SimpleRabbitListenerContainerFactory = SimpleRabbitListenerContainerFactory().apply {
		setConnectionFactory(connectionFactory)
		setMessageConverter(converter)
	}
}
