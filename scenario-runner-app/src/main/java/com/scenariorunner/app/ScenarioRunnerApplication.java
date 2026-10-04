package com.scenariorunner.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppKafkaProperties.class)
public class ScenarioRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScenarioRunnerApplication.class, args);
    }
}
