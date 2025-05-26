package com.sleepkqq.sololeveling.ui.config;

import static com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds.UI_GROUP_ID;

import com.sleepkqq.sololeveling.avro.config.DefaultKafkaConfig;
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@EnableKafka
@Configuration
@SuppressWarnings("unused")
public class KafkaConfig extends DefaultKafkaConfig {

  public KafkaConfig(
      @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
      @Value("${spring.kafka.properties.schema.registry.url}") String schemaRegistryUrl
  ) {
    super(bootstrapServers, schemaRegistryUrl);
  }

  @Bean
  public ConsumerFactory<String, ReceiveNotificationEvent> consumerFactoryReceiveNotificationEvent() {
    return createConsumerFactory(UI_GROUP_ID);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ReceiveNotificationEvent> kafkaListenerContainerFactory(
      ConsumerFactory<String, ReceiveNotificationEvent> consumerFactory
  ) {
    return createKafkaListenerContainerFactory(consumerFactory);
  }
}
