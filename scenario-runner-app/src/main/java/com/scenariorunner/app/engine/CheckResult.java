package com.scenariorunner.app.engine;

import com.fasterxml.jackson.databind.JsonNode;

public record CheckResult(
        String status,
        String reason,
        String key,
        String inputTopic,
        int partition,
        long offset,
        JsonNode payload
) {
}
