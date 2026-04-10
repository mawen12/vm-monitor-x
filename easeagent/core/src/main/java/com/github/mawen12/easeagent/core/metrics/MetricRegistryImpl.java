package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.metrics.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MetricRegistryImpl implements MetricRegistry {
    private final com.codahale.metrics.MetricRegistry metricRegistry;
    private final Map<String, Metric> metricsCache = new HashMap<>();

    private MetricRegistryImpl(com.codahale.metrics.MetricRegistry metricRegistry) {
        this.metricRegistry = Objects.requireNonNull(metricRegistry, "metricRegistry cannot be null");

//        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
//                .convertRatesTo(TimeUnit.SECONDS)
//                .convertDurationsTo(TimeUnit.MILLISECONDS)
//                .build();
//        reporter.start(2, TimeUnit.SECONDS);
//
    }

    public static MetricRegistry build(com.codahale.metrics.MetricRegistry metricRegistry) {
        return metricRegistry == null ? MetricRegistry.NOOP : new MetricRegistryImpl(metricRegistry);
    }

    @Override
    public Counter counter(String name) {
        return getOrAdd(name, () -> CounterImpl.build(metricRegistry.counter(name)));
    }

    @Override
    public Timer timer(String name) {
        return getOrAdd(name, () -> TimerImpl.build(metricRegistry.timer(name)));
    }

    @Override
    public Histogram histogram(String name) {
        return getOrAdd(name, () -> HistogramImpl.build(metricRegistry.histogram(name)));
    }

    @Override
    public Meter meter(String name) {
        return getOrAdd(name, () -> MeterImpl.build(metricRegistry.meter(name)));
    }

    @Override
    public <T> Gauge<T> gauge(String name, MetricSupplier<Gauge<T>> supplier) {
        return getOrAdd(name, () -> metricRegistry.gauge(name, () -> GaugeImpl.build(supplier.newMetric())).getG());
    }

    private <T extends Metric> T getOrAdd(String name, MetricSupplier<T> supplier) {
        Metric metric = metricsCache.get(name);
        if (metric != null) {
            return (T) metric;
        }

        synchronized (metricsCache) {
            metric = metricsCache.get(name);
            if (metric != null) {
                return (T) metric;
            }

            metric = supplier.newMetric();
            metricsCache.put(name, metric);
        }

        return (T) metric;
    }
}
