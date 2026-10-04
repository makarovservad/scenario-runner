package com.scenariorunner.app;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record AppKafkaProperties(String inputTopic, String outputTopic) {
}
