package com.github.mawen12.easeagent.api.metrics;

import java.util.HashMap;

public class MetricName {
    private final Metric.Type metricType;
    private final Metric.SubType metricSubType;
    private final String key;

    public MetricName(Metric.SubType metricSubType, String key, Metric.Type metricType) {
        this.metricType = metricType;
        this.metricSubType = metricSubType;
        this.key = key;
    }

    public Metric.Type getMetricType() {
        return metricType;
    }

    public Metric.SubType getMetricSubType() {
        return metricSubType;
    }

    public String getKey() {
        return key;
    }

    public static MetricName metricNameFor(String name) {
        return new MetricName(
                Metric.SubType.value(name.substring(0, 2)),
                name.substring(3),
                Metric.Type.values()[Integer.parseInt(name.substring(2, 3))]
        );
    }
}
