package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.MetricRegistryManager;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import com.github.mawen12.easeagent.api.metrics.Tags;

import java.util.Map;

@EaseAgentClassLoader("used by bootstrap")
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
    public MetricRegistry newMetricRegistry(Tags tags, Map<String, Object> additionalAttributes, NameFactory nameFactory) {
        com.codahale.metrics.MetricRegistry metricRegistry = new com.codahale.metrics.MetricRegistry();

        AgentSampleBuilder sampleBuilder = new AgentSampleBuilder(additionalAttributes, tags);
        AgentPrometheusExports agentPrometheusExports = new AgentPrometheusExports(metricRegistry, nameFactory, sampleBuilder);
        agentPrometheusExports.register();

        return MetricRegistryImpl.build(metricRegistry);
    }

    @Override
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }
}
