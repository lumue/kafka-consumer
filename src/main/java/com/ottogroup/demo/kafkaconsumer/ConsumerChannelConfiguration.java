package com.ottogroup.demo.kafkaconsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.messaging.PollableChannel;

@Configuration
public class ConsumerChannelConfiguration {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.topic}")
  private String springIntegrationKafkaTopic;

  @Bean
  public PollableChannel consumerChannel() {
    return new QueueChannel();
  }

  @Bean
  public KafkaMessageDrivenChannelAdapter<String,String> kafkaMessageDrivenChannelAdapter(ConcurrentMessageListenerContainer<String,String> kafkaListenerContainer) {
    KafkaMessageDrivenChannelAdapter<String,String> kafkaMessageDrivenChannelAdapter = new KafkaMessageDrivenChannelAdapter<>(
        kafkaListenerContainer);
    kafkaMessageDrivenChannelAdapter.setOutputChannel(consumerChannel());

    return kafkaMessageDrivenChannelAdapter;
  }

  @SuppressWarnings("unchecked")
  @Bean
  public ConcurrentMessageListenerContainer<String,String> kafkaListenerContainer(
      KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<?,?>> containerFactory)
  {
    return (ConcurrentMessageListenerContainer<String, String>) containerFactory.createContainer(springIntegrationKafkaTopic);
  }

  @Bean
  public ConsumerFactory<String,String> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public Map<String,Object> consumerConfigs() {
    Map<String,Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "dummy");
    return properties;
  }
}
