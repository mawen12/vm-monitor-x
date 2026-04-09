package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface NameFactory {

    static Builder createBuilder() {
        return new Builder();
    }

    String counterName(String key, Metric.SubType subType);

    String gaugeName(String key, Metric.SubType subType);

    String histogramName(String key, Metric.SubType subType);

    String meterName(String key, Metric.SubType subType);

    String timerName(String key, Metric.SubType subType);

    class Default implements NameFactory {
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> histograms;
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> counters;
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> timers;
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> gauges;
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> meters;

        public Default(List<Tuple<Metric.SubType, Set<Metric.Field>>> histograms, List<Tuple<Metric.SubType, Set<Metric.Field>>> counters, List<Tuple<Metric.SubType, Set<Metric.Field>>> timers, List<Tuple<Metric.SubType, Set<Metric.Field>>> gauges, List<Tuple<Metric.SubType, Set<Metric.Field>>> meters) {
            this.histograms = histograms;
            this.counters = counters;
            this.timers = timers;
            this.gauges = gauges;
            this.meters = meters;
        }

        @Override
        public String counterName(String key, Metric.SubType subType) {
            return getName(key, Metric.Type.Counter, subType, this.counters);
        }

        @Override
        public String gaugeName(String key, Metric.SubType subType) {
            return getName(key, Metric.Type.Gauge, subType, this.gauges);
        }

        @Override
        public String histogramName(String key, Metric.SubType subType) {
            return getName(key, Metric.Type.Histogram, subType, this.histograms);
        }

        @Override
        public String meterName(String key, Metric.SubType subType) {
            return getName(key, Metric.Type.Meter, subType, this.meters);
        }

        @Override
        public String timerName(String key, Metric.SubType subType) {
            return getName(key, Metric.Type.Timer, subType, this.timers);
        }

        private String getName(String key, Metric.Type metricType, Metric.SubType metricSubType, List<Tuple<Metric.SubType, Set<Metric.Field>>> metrics) {
            for (Tuple<Metric.SubType, Set<Metric.Field>> metric : metrics) {
                if (metric.getX().equals(metricSubType)) {
                    return metricSubType.getCode() + metricType.ordinal() + key;
                }
            }

            throw new IllegalArgumentException("Invalid metricSubType [" + metricSubType.name() + "] of " + metricType.name() + " not be registered in NameFactory");
        }
    }

    class Builder {
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> histograms = new ArrayList<>();
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> counters = new ArrayList<>();
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> timers = new ArrayList<>();
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> gauges = new ArrayList<>();
        private final List<Tuple<Metric.SubType, Set<Metric.Field>>> meters = new ArrayList<>();

        Builder() {}

        public NameFactory build() {
            return new Default(histograms, counters, timers, gauges, meters);
        }

        public Builder meter(Metric.SubType subType, Set<Metric.Field> fields) {
            this.meters.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder timer(Metric.SubType subType, Set<Metric.Field> fields) {
            this.timers.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder counter(Metric.SubType subType, Set<Metric.Field> fields) {
            this.counters.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder histogram(Metric.SubType subType, Set<Metric.Field> fields) {
            this.histograms.add(new Tuple<>(subType, fields));
            return this;
        }

        public Builder gauge(Metric.SubType subType, Set<Metric.Field> fields) {
            this.gauges.add(new Tuple<>(subType, fields));
            return this;
        }
    }
}
