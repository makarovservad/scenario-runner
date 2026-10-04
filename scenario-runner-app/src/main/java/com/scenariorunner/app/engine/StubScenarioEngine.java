package com.scenariorunner.app.engine;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class StubScenarioEngine implements ScenarioEngine {

    static final String STUB_STATUS = "STUB";
    static final String STUB_REASON = "Scenario evaluation not implemented";

    @Override
    public CheckResult evaluate(JsonNode payload, MessageContext context) {
        return new CheckResult(
                STUB_STATUS,
                STUB_REASON,
                context.key(),
                context.topic(),
                context.partition(),
                context.offset(),
                payload
        );
    }
}
