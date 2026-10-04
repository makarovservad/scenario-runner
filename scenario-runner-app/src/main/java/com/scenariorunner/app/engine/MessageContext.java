package com.scenariorunner.app.engine;

public record MessageContext(String key, String topic, int partition, long offset) {
}
