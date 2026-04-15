package com.github.mawen12.easeagent.core.plugins.kafka.metric;

import com.github.mawen12.easeagent.core.plugins.kafka.common.AsyncCallback;
import lombok.Getter;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

@Getter
public class MetricCallback extends AsyncCallback {
    private final long start;
    private final String topic;
    private final KafkaMetric metric;

    public MetricCallback(Callback delegate, String topic, KafkaMetric metric) {
        super(delegate);
        this.topic = topic;
        this.metric = metric;
        this.start = System.currentTimeMillis();
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        try {
            this.metric.collectProducerMetric(topic, System.currentTimeMillis() - start, exception != null);
        } finally {
            if (this.delegate != null) {
                this.delegate.onCompletion(metadata, exception);
            }
        }
    }
}
