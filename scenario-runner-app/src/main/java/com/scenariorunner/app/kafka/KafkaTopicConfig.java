package com.scenariorunner.app.kafka;

import com.scenariorunner.app.AppKafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic inputTopic(AppKafkaProperties properties) {
        return TopicBuilder.name(properties.inputTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic outputTopic(AppKafkaProperties properties) {
        return TopicBuilder.name(properties.outputTopic()).partitions(1).replicas(1).build();
    }
}
