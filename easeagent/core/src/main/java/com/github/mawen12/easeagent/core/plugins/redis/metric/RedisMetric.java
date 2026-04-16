package com.github.mawen12.easeagent.core.plugins.redis.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.Meter;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import com.github.mawen12.easeagent.api.metrics.ServiceMetric;
import com.github.mawen12.easeagent.core.metrics.LastMinutesCounterGauge;

import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.ERROR;

@EaseAgentClassLoader
public class RedisMetric extends ServiceMetric {
    public static final NameFactory NAME_FACTORY = RedisNameFactorySupplier.INSTANCE.nameFactory();

    public RedisMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collect(String key, long duration, boolean success) {
        timer(key, DEFAULT).updateMs(duration);
        counter(key, DEFAULT).inc();
        Meter meter = meter(key, ERROR);
        meter.mark();

        if (!success) {
            counter(key, ERROR).inc();
            meter(key, ERROR).mark();
        }

        gauge(key, DEFAULT, () -> () -> new LastMinutesCounterGauge((long) meter.getOneMinuteRate() * 60, (long) meter.getFiveMinuteRate() * 60 * 5, (long) meter.getFifteenMinuteRate() * 60 * 15));
    }

    public enum RedisNameFactorySupplier implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createDefault();
        }
    }
}
