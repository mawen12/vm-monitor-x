package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import lombok.Getter;

import java.util.Set;

import static com.github.mawen12.easeagent.api.metrics.Metric.*;

@Getter
@SharedToBootstrap
public class MetricName {
    private final Type metricType;
    private final SubType metricSubType;
    private final String key;
    private final Set<FieldWrapper> fields;

    // cache
    private final String name;

    public MetricName(SubType metricSubType, String key, Type metricType, Set<FieldWrapper> fields) {
        this.metricType = metricType;
        this.metricSubType = metricSubType;
        this.key = key;
        this.fields = fields;

        this.name = toName(metricSubType, key, metricType);
    }

    public static MetricName metricNameFor(String name) {
        return new MetricName(
                SubType.value(name.substring(0, 2)),
                name.substring(3),
                Type.values()[Integer.parseInt(name.substring(2, 3))],
                null
        );
    }

    public static String toName(SubType metricSubType, String key, Type metricType) {
        return metricSubType.getCode() + metricType.ordinal() + key;
    }
}
