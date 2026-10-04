package com.scenariorunner.app.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StubScenarioEngineTest {

    private final StubScenarioEngine engine = new StubScenarioEngine();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void evaluateReturnsStubStatusWithoutLoadingScenarios() {
        ObjectNode payload = objectMapper.createObjectNode().put("id", "msg-1");
        MessageContext context = new MessageContext("key-1", "scenario.input", 0, 42L);

        CheckResult result = engine.evaluate(payload, context);

        assertThat(result.status()).isEqualTo("STUB");
        assertThat(result.reason()).isEqualTo("Scenario evaluation not implemented");
        assertThat(result.key()).isEqualTo("key-1");
        assertThat(result.inputTopic()).isEqualTo("scenario.input");
        assertThat(result.partition()).isEqualTo(0);
        assertThat(result.offset()).isEqualTo(42L);
        assertThat(result.payload()).isEqualTo(payload);
    }
}
