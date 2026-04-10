package com.github.mawen12.easeagent.api.metrics;

import java.util.Map;

public interface MetricRegistryManager {
    MetricRegistry getMetricRegistry();

    MetricRegistry newMetricRegistry(Tags tags, Map<String, Object> additionalAttributes, NameFactory nameFactory);
}
