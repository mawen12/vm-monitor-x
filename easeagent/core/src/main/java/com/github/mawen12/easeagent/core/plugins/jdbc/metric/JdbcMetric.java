package com.github.mawen12.easeagent.core.plugins.jdbc.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.metrics.Meter;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import com.github.mawen12.easeagent.api.metrics.ServiceMetric;
import com.github.mawen12.easeagent.api.utils.ContextUtils;
import com.github.mawen12.easeagent.core.metrics.LastMinutesCounterGauge;

import java.time.Duration;

import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.ERROR;

@EaseAgentClassLoader
public class JdbcMetric extends ServiceMetric {
    public static final NameFactory NAME_FACTORY = JdbcNameFactory.INSTANCE.nameFactory();

    public JdbcMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collectMetric(String key, boolean success, Context ctx) {
        timer(key, DEFAULT).update(Duration.ofMillis(ContextUtils.getDuration(ctx)));
        counter(key, DEFAULT).inc();
        Meter meter = meter(key, DEFAULT);
        meter.mark();

        if (!success) {
            counter(key, ERROR).inc();
            meter(key, ERROR).mark();
        }

        // meter 仅统计每x分钟的事件速率，单位为事件数/秒
        gauge(key, DEFAULT, () -> () -> new LastMinutesCounterGauge((long) meter.getOneMinuteRate() * 60, (long) meter.getFiveMinuteRate() * 60 * 5, (long) meter.getFifteenMinuteRate() * 60 * 15));
    }

    enum JdbcNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createDefault();
        }
    }
}
