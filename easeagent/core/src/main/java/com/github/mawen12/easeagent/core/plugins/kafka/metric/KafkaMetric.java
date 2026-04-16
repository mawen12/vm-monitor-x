package com.github.mawen12.easeagent.core.plugins.kafka.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.Meter;
import com.github.mawen12.easeagent.api.metrics.MetricRegistry;
import com.github.mawen12.easeagent.api.metrics.NameFactory;
import com.github.mawen12.easeagent.api.metrics.ServiceMetric;

import static com.github.mawen12.easeagent.api.metrics.Metric.Field.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.FieldWrapper.of;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.ValueFetcher.*;

@EaseAgentClassLoader
public class KafkaMetric extends ServiceMetric {

    public KafkaMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public void collectProducerMetric(String topic, long duration, boolean success) {
        Meter meter = this.meter(topic, PRODUCER);
        meter.mark();
        this.timer(topic, PRODUCER).updateMs(duration);
        this.counter(topic, PRODUCER).inc();

        if (!success) {
            this.counter(topic, PRODUCER_ERROR).inc();
            this.meter(topic, PRODUCER_ERROR).mark();
        }
    }

    public void collectConsumeMetric(String topic) {
        this.meter(topic, CONSUMER).mark();
        this.counter(topic, CONSUMER).inc();
    }

    public void collectConsumeMetric(String topic, long duration, boolean success) {
        this.meter(topic, CONSUMER).mark();
        this.counter(topic, CONSUMER).inc();
        this.timer(topic, CONSUMER).updateMs(duration);

        if (!success) {
            this.meter(topic, CONSUMER_ERROR).mark();
            this.counter(topic, CONSUMER_ERROR).inc();
        }
    }


    public enum KafkaNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .counter(PRODUCER, of(EXECUTION_PRODUCER_COUNT, CountingCount))
                    .counter(PRODUCER_ERROR, of(EXECUTION_PRODUCER_ERROR_COUNT, CountingCount))
                    .counter(CONSUMER, of(EXECUTION_CONSUMER_COUNT, CountingCount))
                    .counter(CONSUMER_ERROR, of(EXECUTION_CONSUMER_ERROR_COUNT, CountingCount))
                    .meter(PRODUCER,
                            of(PRODUCER_M1_RATE, MeterM1Rate),
                            of(PRODUCER_M5_RATE, MeterM5Rate),
                            of(PRODUCER_M15_RATE, MeterM15Rate)
                    )
                    .meter(PRODUCER_ERROR,
                            of(PRODUCER_M1_ERROR_RATE, MeterM1Rate),
                            of(PRODUCER_M5_ERROR_RATE, MeterM5Rate),
                            of(PRODUCER_M15_ERROR_RATE, MeterM15Rate)
                    )
                    .meter(CONSUMER,
                            of(CONSUMER_M1_RATE, MeterM1Rate),
                            of(CONSUMER_M5_RATE, MeterM5Rate),
                            of(CONSUMER_M15_RATE, MeterM15Rate)
                    )
                    .meter(CONSUMER_ERROR,
                            of(CONSUMER_M1_ERROR_RATE, MeterM1Rate),
                            of(CONSUMER_M5_ERROR_RATE, MeterM5Rate),
                            of(CONSUMER_M15_ERROR_RATE, MeterM15Rate)
                    )
                    .timer(PRODUCER,
                            of(PRODUCER_MIN_EXECUTION_TIME, SnapshotMinValue),
                            of(PRODUCER_MAX_EXECUTION_TIME, SnapshotMaxValue),
                            of(PRODUCER_MEAN_EXECUTION_TIME, SnapshotMeanValue),
                            of(PRODUCER_P25_EXECUTION_TIME, Snapshot25thPercentileValue),
                            of(PRODUCER_P50_EXECUTION_TIME, Snapshot50thPercentileValue),
                            of(PRODUCER_P75_EXECUTION_TIME, Snapshot75thPercentileValue),
                            of(PRODUCER_P95_EXECUTION_TIME, Snapshot95thPercentileValue),
                            of(PRODUCER_P98_EXECUTION_TIME, Snapshot98thPercentileValue),
                            of(PRODUCER_P99_EXECUTION_TIME, Snapshot99thPercentileValue),
                            of(PRODUCER_P999_EXECUTION_TIME, Snapshot999thPercentileValue)
                    )
                    .timer(CONSUMER,
                            of(CONSUMER_MIN_EXECUTION_TIME, SnapshotMinValue),
                            of(CONSUMER_MAX_EXECUTION_TIME, SnapshotMaxValue),
                            of(CONSUMER_MEAN_EXECUTION_TIME, SnapshotMeanValue),
                            of(CONSUMER_P25_EXECUTION_TIME, Snapshot25thPercentileValue),
                            of(CONSUMER_P50_EXECUTION_TIME, Snapshot50thPercentileValue),
                            of(CONSUMER_P75_EXECUTION_TIME, Snapshot75thPercentileValue),
                            of(CONSUMER_P95_EXECUTION_TIME, Snapshot95thPercentileValue),
                            of(CONSUMER_P98_EXECUTION_TIME, Snapshot98thPercentileValue),
                            of(CONSUMER_P99_EXECUTION_TIME, Snapshot99thPercentileValue),
                            of(CONSUMER_P999_EXECUTION_TIME, Snapshot999thPercentileValue)
                    )
                    .build();
        }
    }
}
