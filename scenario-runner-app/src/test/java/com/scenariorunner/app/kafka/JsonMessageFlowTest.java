package com.scenariorunner.app.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scenariorunner.app.engine.CheckResult;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"scenario.input", "scenario.result"})
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
class JsonMessageFlowTest {

    @Autowired
    private KafkaTemplate<String, JsonNode> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void inputMessageProducesStubResult() {
        JsonNode payload = objectMapper.createObjectNode().put("hello", "world");
        kafkaTemplate.send("scenario.input", "corr-1", payload);
        kafkaTemplate.flush();

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-result-reader", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, CheckResult.class.getName());

        DefaultKafkaConsumerFactory<String, CheckResult> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(CheckResult.class, false)
        );

        try (Consumer<String, CheckResult> consumer = consumerFactory.createConsumer()) {
            embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "scenario.result");
            ConsumerRecord<String, CheckResult> record = KafkaTestUtils.getSingleRecord(
                    consumer,
                    "scenario.result",
                    Duration.ofSeconds(15)
            );

            assertThat(record.key()).isEqualTo("corr-1");
            assertThat(record.value().status()).isEqualTo("STUB");
            assertThat(record.value().reason()).isEqualTo("Scenario evaluation not implemented");
            assertThat(record.value().payload().path("hello").asText()).isEqualTo("world");
        }
    }
}
