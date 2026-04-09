package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.MetricRegistryManager;

public class MetricRegistryManagerImpl implements MetricRegistryManager {

    private final MetricRegistry metricRegistry;

    private MetricRegistryManagerImpl(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    public static MetricRegistryManager build() {
        com.codahale.metrics.MetricRegistry mr = new com.codahale.metrics.MetricRegistry();
        return new MetricRegistryManagerImpl(MetricRegistryImpl.build(mr));
    }

    @Override
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }
}
