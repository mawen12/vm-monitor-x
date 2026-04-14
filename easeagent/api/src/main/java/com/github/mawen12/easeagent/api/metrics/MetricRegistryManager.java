package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

import java.util.Map;

@SharedToBootstrap
public interface MetricRegistryManager {
    MetricRegistry getMetricRegistry();

    MetricRegistry newMetricRegistry(Tags tags, Map<String, Object> additionalAttributes, NameFactory nameFactory);
}
