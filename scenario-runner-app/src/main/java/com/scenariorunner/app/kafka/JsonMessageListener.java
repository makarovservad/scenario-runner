package com.scenariorunner.app.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.scenariorunner.app.engine.CheckResult;
import com.scenariorunner.app.engine.MessageContext;
import com.scenariorunner.app.engine.ScenarioEngine;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class JsonMessageListener {

    private static final Logger log = LoggerFactory.getLogger(JsonMessageListener.class);

    private final ScenarioEngine scenarioEngine;
    private final CheckResultProducer checkResultProducer;

    public JsonMessageListener(ScenarioEngine scenarioEngine, CheckResultProducer checkResultProducer) {
        this.scenarioEngine = scenarioEngine;
        this.checkResultProducer = checkResultProducer;
    }

    @KafkaListener(topics = "${app.kafka.input-topic}")
    public void onMessage(ConsumerRecord<String, JsonNode> record) {
        try {
            MessageContext context = new MessageContext(
                    record.key(),
                    record.topic(),
                    record.partition(),
                    record.offset()
            );
            CheckResult result = scenarioEngine.evaluate(record.value(), context);
            checkResultProducer.publish(result);
        } catch (RuntimeException ex) {
            log.error(
                    "Failed to process message from topic={} partition={} offset={}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    ex
            );
        }
    }
}
