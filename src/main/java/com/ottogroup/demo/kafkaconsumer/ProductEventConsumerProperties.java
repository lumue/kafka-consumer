package com.ottogroup.demo.kafkaconsumer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("producteventconsumer")
public class ProductEventConsumerProperties {

  private String topic;


  public String getTopic() {
    return topic;
  }

  public void setTopic(final String topic) {
    this.topic = topic;
  }
}
