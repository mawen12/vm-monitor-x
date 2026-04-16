package com.github.mawen12.easeagent.core.plugins.mongodb.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.Meter;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import com.github.mawen12.easeagent.api.metrics.ServiceMetric;
import com.github.mawen12.easeagent.core.metrics.LastMinutesCounterGauge;

import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.ERROR;

@EaseAgentClassLoader
public class MongoMetric extends ServiceMetric {

    public MongoMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collectMetric(String key, long duration, boolean success) {
        Meter meter = this.meter(key, DEFAULT);
        meter.mark();
        this.counter(key, DEFAULT).inc();

        if (!success) {
            this.meter(key, ERROR).mark();
            this.counter(key, ERROR).inc();
        }

        this.gauge(key, DEFAULT, () -> () -> new LastMinutesCounterGauge((long) meter.getOneMinuteRate() * 60, (long) meter.getFiveMinuteRate() * 60 * 5, (long) meter.getFifteenMinuteRate() * 60 * 15));
    }

    public enum MongoNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createDefault();
        }
    }
}
