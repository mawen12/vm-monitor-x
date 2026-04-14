package com.github.mawen12.easeagent.core.metrics;

import com.codahale.metrics.*;
import com.codahale.metrics.Timer;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.GaugeMetricModel;
import com.github.mawen12.easeagent.api.metrics.Metric;
import com.github.mawen12.easeagent.api.metrics.MetricName;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import io.prometheus.client.Collector;
import io.prometheus.client.dropwizard.samplebuilder.SampleBuilder;

import java.util.*;

@EaseAgentClassLoader
public class AgentPrometheusExports extends Collector implements Collector.Describable {
    private static final MetricFilter METRIC_FILTER = MetricFilter.ALL;

    private final MetricRegistry metricRegistry;
    private final NameFactory nameFactory;
    private final SampleBuilder sampleBuilder;

    private final TimerExports timerExports = new TimerExports();
    private final CounterExports counterExports = new CounterExports();
    private final MeterExports meterExports = new MeterExports();

    private final GaugeExports gaugeExports = new GaugeExports();
//    private final HistogramExports histogramExports = new HistogramExports();

    public AgentPrometheusExports(MetricRegistry metricRegistry, NameFactory nameFactory, SampleBuilder sampleBuilder) {
        this.metricRegistry = metricRegistry;
        this.nameFactory = nameFactory;
        this.sampleBuilder = sampleBuilder;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        Map<String, MetricFamilySamples> mfSamplesMap = new HashMap<>();

        gaugeExports.addToMap(mfSamplesMap);
        counterExports.addToMap(mfSamplesMap);
        meterExports.addToMap(mfSamplesMap);
        timerExports.addToMap(mfSamplesMap);
//        histogramExports.addToMap(mfSamplesMap);
        return new ArrayList<>(mfSamplesMap.values());
    }

    protected void addToMap(Map<String, MetricFamilySamples> mfSamplesMap, MetricFamilySamples newMfSamples) {
        if (newMfSamples != null) {
            MetricFamilySamples current = mfSamplesMap.get(newMfSamples.name);
            if (current != null) {
                List<MetricFamilySamples.Sample> samples = new ArrayList<>(current.samples);
                samples.addAll(newMfSamples.samples);
                mfSamplesMap.put(newMfSamples.name, new MetricFamilySamples(newMfSamples.name, current.type, current.help, samples));
            } else {
                mfSamplesMap.put(newMfSamples.name, newMfSamples);
            }
        }
    }


    @Override
    public List<MetricFamilySamples> describe() {
        return Collections.emptyList();
    }

    abstract class Exports<T extends com.codahale.metrics.Metric> {
        protected final Collector.Type type;
        protected final Class<?> clazz;

        public Exports(Type type, Class<?> clazz) {
            this.type = type;
            this.clazz = clazz;
        }

        protected void addToMap(Map<String, MetricFamilySamples> mfSamplesMap) {
            SortedMap<String, T> metrics = getMetrics();
            consumeMetrics(mfSamplesMap, metrics);
        }

        /**
         *
         * @param name 记录的 key
         * @param value 记录的值
         * @param fieldType 字段的键
         * @return
         */
        protected MetricFamilySamples.Sample createSample(String name, Object value, String fieldType) {
            double dv = 0.0D;
            if (value instanceof Number) {
                dv = ((Number) value).doubleValue();
            } else if (value instanceof Boolean) {
                dv = (Boolean) value ? 1.0D : 0.0D;
            }
            return sampleBuilder.createSample(name, "_" + fieldType, Collections.emptyList(), Collections.emptyList(), dv);
        }

        protected void consumeMetric(Map<String, MetricFamilySamples> mfSamplesMap, MetricName metricName, Metric metric) {
            for (Metric.FieldWrapper fieldWrapper : metricName.getFields()) {
                MetricFamilySamples.Sample sample = createSample(metricName.getName(), fieldWrapper.getValueFetcher().apply(metric), fieldWrapper.getField().getField());
                MetricFamilySamples newMfSamples = new MetricFamilySamples(sample.name, type, getHelpMessage(sample.name), Collections.singletonList(sample));
                AgentPrometheusExports.this.addToMap(mfSamplesMap, newMfSamples);
            }
        }

        protected String getHelpMessage(String metricName) {
            return String.format("Generated from Dropwizard metric import (metric=%s, type=%s)", metricName, clazz.getName());
        }

        protected abstract SortedMap<String, T> getMetrics();

        protected abstract void consumeMetrics(Map<String, MetricFamilySamples> mfSamplesMap, SortedMap<String, T> metrics);
    }

    class CounterExports extends Exports<Counter> {
        public CounterExports() {
            super(Type.SUMMARY, Counter.class);
        }

        @Override
        protected SortedMap<String, Counter> getMetrics() {
            return metricRegistry.getCounters(METRIC_FILTER);
        }

        @Override
        protected void consumeMetrics(Map<String, MetricFamilySamples> mfSamplesMap, SortedMap<String, Counter> metrics) {
            for (Map.Entry<String, Counter> entry : metrics.entrySet()) {
                MetricName metricName = MetricName.metricNameFor(entry.getKey());
                Map<Metric.SubType, MetricName> map = nameFactory.counterNames(metricName.getKey());
                MetricName actualMetricName = map.get(metricName.getMetricSubType());

                consumeMetric(mfSamplesMap, actualMetricName, CounterImpl.build(metrics.get(actualMetricName.getName())));
            }
        }
    }

    class TimerExports extends Exports<Timer> {
        public TimerExports() {
            super(Type.SUMMARY, Timer.class);
        }

        @Override
        protected SortedMap<String, Timer> getMetrics() {
            return metricRegistry.getTimers(METRIC_FILTER);
        }

        @Override
        protected void consumeMetrics(Map<String, MetricFamilySamples> mfSamplesMap, SortedMap<String, Timer> metrics) {
            for (Map.Entry<String, Timer> entry : metrics.entrySet()) {
                MetricName metricName = MetricName.metricNameFor(entry.getKey());
                Map<Metric.SubType, MetricName> map = nameFactory.timerNames(metricName.getKey());
                MetricName actualMetricName = map.get(metricName.getMetricSubType());

                consumeMetric(mfSamplesMap, actualMetricName, TimerImpl.build(metrics.get(actualMetricName.getName())));
            }
        }
    }

    class MeterExports extends Exports<Meter> {
        public MeterExports() {
            super(Type.SUMMARY, Meter.class);
        }

        @Override
        protected SortedMap<String, Meter> getMetrics() {
            return metricRegistry.getMeters(METRIC_FILTER);
        }

        @Override
        protected void consumeMetrics(Map<String, MetricFamilySamples> mfSamplesMap, SortedMap<String, Meter> metrics) {
            for (Map.Entry<String, Meter> entry : metrics.entrySet()) {
                MetricName metricName = MetricName.metricNameFor(entry.getKey());
                Map<Metric.SubType, MetricName> map = nameFactory.meterNames(metricName.getKey());
                MetricName actualMetricName = map.get(metricName.getMetricSubType());

                consumeMetric(mfSamplesMap, actualMetricName, MeterImpl.build(metrics.get(actualMetricName.getName())));
            }
        }
    }

    class HistogramExports extends Exports<Histogram> {
        public HistogramExports() {
            super(Type.SUMMARY, Histogram.class);
        }

        @Override
        protected SortedMap<String, Histogram> getMetrics() {
            return metricRegistry.getHistograms(METRIC_FILTER);
        }

        @Override
        protected void consumeMetrics(Map<String, MetricFamilySamples> mfSamplesMap, SortedMap<String, Histogram> metrics) {
            for (Map.Entry<String, Histogram> entry : metrics.entrySet()) {
                MetricName metricName = MetricName.metricNameFor(entry.getKey());
                Map<Metric.SubType, MetricName> map = nameFactory.histogramNames(metricName.getKey());
                MetricName actualMetricName = map.get(metricName.getMetricSubType());

                consumeMetric(mfSamplesMap, actualMetricName, HistogramImpl.build(metrics.get(actualMetricName.getName())));
            }
        }
    }

    class GaugeExports extends Exports<Gauge> {
        public GaugeExports() {
            super(Type.GAUGE, Histogram.class);
        }

        @Override
        protected SortedMap<String, Gauge> getMetrics() {
            return metricRegistry.getGauges(METRIC_FILTER);
        }

        @Override
        protected void consumeMetrics(Map<String, MetricFamilySamples> mfSamplesMap, SortedMap<String, Gauge> metrics) {
            for (Map.Entry<String, Gauge> entry : metrics.entrySet()) {
                MetricName metricName = MetricName.metricNameFor(entry.getKey());
                Map<Metric.SubType, MetricName> map = nameFactory.gaugeNames(metricName.getKey());
                MetricName actualMetricName = map.get(metricName.getMetricSubType());

                consumeMetric(mfSamplesMap, actualMetricName, metrics.get(actualMetricName.getName()));
            }
        }

        protected void consumeMetric(Map<String, MetricFamilySamples> mfSamplesMap, MetricName metricName, Gauge metric) {
            if (metric == null) {
                return;
            }

            Object value = metric.getValue();
            if (value instanceof GaugeMetricModel) {
                GaugeMetricModel model = (GaugeMetricModel) value;
                for (Map.Entry<String, Object> entry : model.toHashMap().entrySet()) {
                    consumeEntry(mfSamplesMap, metricName, entry.getKey(), entry.getValue());
                }
            } else if (value instanceof Number || value instanceof Boolean) {
                consumeEntry(mfSamplesMap, metricName, "value", value);
            } else {
                consumeEntry(mfSamplesMap, metricName, "value", Objects.toString(value));
            }
        }

        protected void consumeEntry(Map<String, MetricFamilySamples> mfSamplesMap, MetricName metricName, String key, Object value) {
            MetricFamilySamples.Sample sample = createSample(metricName.getName(), value, key);
            MetricFamilySamples newMfSamples = new MetricFamilySamples(sample.name, type, getHelpMessage(sample.name), Collections.singletonList(sample));
            AgentPrometheusExports.this.addToMap(mfSamplesMap, newMfSamples);
        }
    }
}
