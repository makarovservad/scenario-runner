package com.scenariorunner.app.engine;

import com.fasterxml.jackson.databind.JsonNode;

public interface ScenarioEngine {

    CheckResult evaluate(JsonNode payload, MessageContext context);
}
