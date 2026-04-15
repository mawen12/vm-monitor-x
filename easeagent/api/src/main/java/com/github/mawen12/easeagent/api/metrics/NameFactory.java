package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.utils.Sets;
import com.github.mawen12.easeagent.api.utils.Tuple;

import java.util.*;

import static com.github.mawen12.easeagent.api.metrics.Metric.Field.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.FieldWrapper.of;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.ERROR;
import static com.github.mawen12.easeagent.api.metrics.Metric.ValueFetcher.*;

@SharedToBootstrap
public interface NameFactory {

    static Builder createBuilder() {
        return new Builder();
    }

    static NameFactory createDefault() {
        return NameFactory.createBuilder()
                .timer(DEFAULT,
                        of(MIN_EXECUTION_TIME, SnapshotMinValue),
                        of(MAX_EXECUTION_TIME, SnapshotMaxValue),
                        of(MEAN_EXECUTION_TIME, SnapshotMeanValue),
                        of(P25_EXECUTION_TIME, Snapshot25thPercentileValue),
                        of(P50_EXECUTION_TIME, Snapshot50thPercentileValue),
                        of(P75_EXECUTION_TIME, Snapshot75thPercentileValue),
                        of(P95_EXECUTION_TIME, Snapshot95thPercentileValue),
                        of(P98_EXECUTION_TIME, Snapshot98thPercentileValue),
                        of(P99_EXECUTION_TIME, Snapshot99thPercentileValue),
                        of(P999_EXECUTION_TIME, Snapshot999thPercentileValue)
                )
                .gauge(DEFAULT, new HashSet<>())
                .meter(DEFAULT,
                        of(M1_RATE, MeterM1Rate),
                        of(M5_RATE, MeterM5Rate),
                        of(M15_RATE, MeterM15Rate)
                )
                .meter(ERROR,
                        of(M1_ERR_RATE, MeterM1Rate),
                        of(M5_ERR_RATE, MeterM5Rate),
                        of(M15_ERR_RATE, MeterM15Rate)
                )
                .counter(DEFAULT,
                        of(EXECUTION_COUNT, CountingCount)
                )
                .counter(ERROR,
                        of(EXECUTION_ERR_COUNT, CountingCount)
                )
                .build();
    }


    String counterName(String key, SubType subType);

    String gaugeName(String key, SubType subType);

    String histogramName(String key, SubType subType);

    String meterName(String key, SubType subType);

    String timerName(String key, SubType subType);

    // ===================================================

    Map<SubType, MetricName> counterNames(String key);

    Map<SubType, MetricName> gaugeNames(String key);

    Map<SubType, MetricName> histogramNames(String key);

    Map<SubType, MetricName> meterNames(String key);

    Map<SubType, MetricName> timerNames(String key);

    interface Supplier {
        NameFactory nameFactory();
    }

    class NameFactoryImpl implements NameFactory {
        private final List<Tuple<SubType, Set<FieldWrapper>>> histograms;
        private final List<Tuple<SubType, Set<FieldWrapper>>> counters;
        private final List<Tuple<SubType, Set<FieldWrapper>>> timers;
        private final List<Tuple<SubType, Set<FieldWrapper>>> gauges;
        private final List<Tuple<SubType, Set<FieldWrapper>>> meters;

        public NameFactoryImpl(List<Tuple<SubType, Set<FieldWrapper>>> histograms, List<Tuple<SubType, Set<FieldWrapper>>> counters, List<Tuple<SubType, Set<FieldWrapper>>> timers, List<Tuple<SubType, Set<FieldWrapper>>> gauges, List<Tuple<SubType, Set<FieldWrapper>>> meters) {
            this.histograms = histograms;
            this.counters = counters;
            this.timers = timers;
            this.gauges = gauges;
            this.meters = meters;
        }

        @Override
        public String counterName(String key, SubType subType) {
            return getName(key, Type.Counter, subType, this.counters);
        }

        @Override
        public String gaugeName(String key, SubType subType) {
            return getName(key, Type.Gauge, subType, this.gauges);
        }

        @Override
        public String histogramName(String key, SubType subType) {
            return getName(key, Type.Histogram, subType, this.histograms);
        }

        @Override
        public String meterName(String key, SubType subType) {
            return getName(key, Type.Meter, subType, this.meters);
        }

        @Override
        public String timerName(String key, SubType subType) {
            return getName(key, Type.Timer, subType, this.timers);
        }

        @Override
        public Map<SubType, MetricName> counterNames(String key) {
            Map<SubType, MetricName> results = new HashMap<>();
            counters.forEach(t -> results.put(t.getX(), new MetricName(t.getX(), key, Type.Counter, t.getY())));
            return results;
        }

        @Override
        public Map<SubType, MetricName> gaugeNames(String key) {
            Map<SubType, MetricName> results = new HashMap<>();
            gauges.forEach(t -> results.put(t.getX(), new MetricName(t.getX(), key, Type.Gauge, t.getY())));
            return results;
        }

        @Override
        public Map<SubType, MetricName> histogramNames(String key) {
            Map<SubType, MetricName> results = new HashMap<>();
            histograms.forEach(t -> results.put(t.getX(), new MetricName(t.getX(), key, Type.Histogram, t.getY())));
            return results;
        }

        @Override
        public Map<SubType, MetricName> meterNames(String key) {
            Map<SubType, MetricName> results = new HashMap<>();
            meters.forEach(t -> results.put(t.getX(), new MetricName(t.getX(), key, Type.Meter, t.getY())));
            return results;
        }

        @Override
        public Map<SubType, MetricName> timerNames(String key) {
            Map<SubType, MetricName> results = new HashMap<>();
            timers.forEach(t -> results.put(t.getX(), new MetricName(t.getX(), key, Type.Timer, t.getY())));
            return results;
        }

        private String getName(String key, Type metricType, SubType metricSubType, List<Tuple<SubType, Set<FieldWrapper>>> metrics) {
            for (Tuple<SubType, Set<FieldWrapper>> metric : metrics) {
                if (metric.getX().equals(metricSubType)) {
                    return metricSubType.getCode() + metricType.ordinal() + key;
                }
            }

            throw new IllegalArgumentException("Invalid metricSubType [" + metricSubType.name() + "] of " + metricType.name() + " not be registered in NameFactory");
        }
    }

    class Builder {
        private final List<Tuple<SubType, Set<FieldWrapper>>> histograms = new ArrayList<>();
        private final List<Tuple<SubType, Set<FieldWrapper>>> counters = new ArrayList<>();
        private final List<Tuple<SubType, Set<FieldWrapper>>> timers = new ArrayList<>();
        private final List<Tuple<SubType, Set<FieldWrapper>>> gauges = new ArrayList<>();
        private final List<Tuple<SubType, Set<FieldWrapper>>> meters = new ArrayList<>();

        Builder() {
        }

        public NameFactory build() {
            return new NameFactoryImpl(histograms, counters, timers, gauges, meters);
        }

        public Builder meter(SubType subType, Set<FieldWrapper> fields) {
            this.meters.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder meter(SubType subType, FieldWrapper... fs) {
            return this.meter(subType, Sets.of(fs));
        }

        public Builder timer(SubType subType, Set<FieldWrapper> fields) {
            this.timers.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder timer(SubType subType, FieldWrapper... fs) {
            return this.timer(subType, Sets.of(fs));
        }

        public Builder counter(SubType subType, Set<FieldWrapper> fields) {
            this.counters.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder counter(SubType subType, FieldWrapper... fs) {
            return this.counter(subType, Sets.of(fs));
        }

        public Builder histogram(SubType subType, Set<FieldWrapper> fields) {
            this.histograms.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder histogram(SubType subType, FieldWrapper... fs) {
            return this.histogram(subType, Sets.of(fs));
        }

        public Builder gauge(SubType subType, Set<FieldWrapper> fields) {
            this.gauges.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder gauge(SubType subType, FieldWrapper... fs) {
            return this.gauge(subType, Sets.of(fs));
        }
    }
}
