package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.MetricName;
import com.github.mawen12.easeagent.api.metrics.Tags;
import io.prometheus.client.Collector;
import io.prometheus.client.dropwizard.samplebuilder.DefaultSampleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EaseAgentClassLoader
public class AgentSampleBuilder extends DefaultSampleBuilder {
    private final Map<String, Object> additionalAttributes;
    private final Tags tags;

    public AgentSampleBuilder(Map<String, Object> additionalAttributes, Tags tags) {
        this.additionalAttributes = additionalAttributes;
        this.tags = tags;
    }

    @Override
    public Collector.MetricFamilySamples.Sample createSample(String dropwizardName, String nameSuffix, List<String> additionalLabelNames, List<String> additionalLabelValues, double value) {
        List<String> labelNames = new ArrayList<>(additionalLabelNames);
        List<String> labelValues = new ArrayList<>(additionalLabelValues);

        additionalAttributes(labelNames, labelValues);
        String name = rebuildName(dropwizardName, labelNames, labelValues);

        return super.createSample(name, nameSuffix, labelNames, labelValues, value);
    }

    private void additionalAttributes(List<String> labelNames, List<String> labelValues) {
        if (additionalAttributes == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : additionalAttributes.entrySet()) {
            labelNames.add(entry.getKey());
            labelValues.add(entry.getValue().toString());
        }
    }

    private String rebuildName(String dropwizardName, List<String> labelNames, List<String> labelValues) {
        MetricName metricName = MetricName.metricNameFor(dropwizardName);
        labelNames.add("Type");
        labelNames.add("SubType");
        labelNames.add(tags.getKeyFieldName());
        labelValues.add(metricName.getMetricType().name());
        labelValues.add(metricName.getMetricSubType().name());
        labelValues.add(metricName.getKey());

        return tags.getCategory() + "." + tags.getType();
    }
}
