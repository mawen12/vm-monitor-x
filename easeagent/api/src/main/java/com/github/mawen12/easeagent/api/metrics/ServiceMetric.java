package com.github.mawen12.easeagent.api.metrics;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ServiceMetric {
    protected final MetricRegistry metricRegistry;
    protected final NameFactory nameFactory;

    public Counter counter(String key, Metric.SubType subType) {
        return metricRegistry.counter(nameFactory.counterName(key, subType));
    }

    public <T> Gauge<T> gauge(String key, Metric.SubType subType, MetricSupplier<Gauge<T>> supplier) {
        return metricRegistry.gauge(nameFactory.gaugeName(key, subType), supplier);
    }

    public Histogram histogram(String key, Metric.SubType subType) {
        return metricRegistry.histogram(nameFactory.histogramName(key, subType));
    }

    public Meter meter(String key, Metric.SubType subType) {
        return metricRegistry.meter(nameFactory.meterName(key, subType));
    }

    public Timer timer(String key, Metric.SubType subType) {
        return metricRegistry.timer(nameFactory.timerName(key, subType));
    }
}
