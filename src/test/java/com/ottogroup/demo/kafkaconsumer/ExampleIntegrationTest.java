package com.ottogroup.demo.kafkaconsumer;




import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = KafkaConsumerApplication.class)
@ActiveProfiles("test")
@Testcontainers
public class ExampleIntegrationTest {

  @Container
  static KafkaContainer kafkaContainer = new KafkaContainer("5.2.1");

  KafkaTemplate<String,String> kafkaTemplate;

  @Autowired
  PollableChannel consumerChannel;

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    kafkaContainer.start();
    registry.add("spring.kafka.bootstrap.servers", kafkaContainer::getBootstrapServers);
    registry.add("spring.kafka.consumer.properties.auto.offset.reset", () -> "earliest");
  }

  @Test
  public void should_consume_product_events(){
    consumerChannel.receive(5000);
  }

}
