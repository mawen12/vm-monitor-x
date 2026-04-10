package com.github.mawen12.easeagent.core.plugins.jdbc;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.metrics.*;
import com.github.mawen12.easeagent.api.utils.ContextUtils;
import com.github.mawen12.easeagent.api.utils.Sets;

import java.time.Duration;

import static com.github.mawen12.easeagent.api.metrics.Metric.Field.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.FieldWrapper.of;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.ERROR;
import static com.github.mawen12.easeagent.api.metrics.Metric.ValueFetcher.*;

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

        gauge(key, DEFAULT, () -> () -> new GaugeMetricModel.LastMinutesCounterGauge(meter));
    }

    enum JdbcNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
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
                    .gauge(DEFAULT, Sets.of())
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
    }
}
