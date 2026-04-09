package com.github.mawen12.easeagent.core.plugins.jdbc;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.metrics.*;
import com.github.mawen12.easeagent.api.utils.ContextUtils;
import com.github.mawen12.easeagent.core.utils.Sets;

import java.time.Duration;

import static com.github.mawen12.easeagent.api.metrics.Metric.Field.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.ERROR;

public class JdbcMetric extends ServiceMetric {
    private static JdbcMetric INSTANCE;

    public static JdbcMetric init(MetricRegistry metricRegistry, NameFactory nameFactory) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (JdbcMetric.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new JdbcMetric(metricRegistry, nameFactory);
        }
        return INSTANCE;
    }

    private JdbcMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
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

        gauge(key, DEFAULT, () -> () -> new GaugeMetricModel.LastMinutesCounterGauge(meter));
    }

    public static NameFactory nameFactory() {
        return NameFactory.createBuilder()
                .timer(DEFAULT, Sets.of(
                        MIN_EXECUTION_TIME,
                        MAX_EXECUTION_TIME,
                        MEAN_EXECUTION_TIME,
                        P25_EXECUTION_TIME,
                        P50_EXECUTION_TIME,
                        P75_EXECUTION_TIME,
                        P95_EXECUTION_TIME,
                        P98_EXECUTION_TIME,
                        P99_EXECUTION_TIME,
                        P999_EXECUTION_TIME
                ))
                .gauge(DEFAULT, Sets.of())
                .meter(DEFAULT, Sets.of(
                        M1_RATE,
                        M5_RATE,
                        M15_RATE
                ))
                .meter(ERROR, Sets.of(
                        M1_ERR_RATE,
                        M5_ERR_RATE,
                        M15_ERR_RATE
                ))
                .counter(DEFAULT, Sets.of(
                        EXECUTION_COUNT
                ))
                .counter(ERROR, Sets.of(
                        EXECUTION_ERR_COUNT
                ))
                .build();
    }

}
