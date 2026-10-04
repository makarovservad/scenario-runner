package com.scenariorunner.app.kafka;

import com.scenariorunner.app.AppKafkaProperties;
import com.scenariorunner.app.engine.CheckResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CheckResultProducer {

    private final KafkaTemplate<String, CheckResult> kafkaTemplate;
    private final AppKafkaProperties kafkaProperties;

    public CheckResultProducer(
            KafkaTemplate<String, CheckResult> kafkaTemplate,
            AppKafkaProperties kafkaProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
    }

    public void publish(CheckResult result) {
        kafkaTemplate.send(kafkaProperties.outputTopic(), result.key(), result);
    }
}
