package com.github.mawen12.easeagent.core.plugins.kafka.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.metrics.ServiceMetricRegistry;
import com.github.mawen12.easeagent.api.metrics.Tags;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

@EaseAgentClassLoader
public class KafkaConsumerMetricInterceptor implements NonReentrantInterceptor {
    private KafkaMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "kafka", "resource");
        metric = ServiceMetricRegistry.getOrCreate(tags, KafkaMetric.KafkaNameFactory.INSTANCE, KafkaMetric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {

    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        if (!methodInfo.isSuccess()) {
            return;
        }

        ConsumerRecords<?, ?> consumerRecords = (ConsumerRecords<?, ?>) methodInfo.getRetValue();
        if (consumerRecords == null || consumerRecords.isEmpty()) {
            return;
        }

        for (ConsumerRecord<?, ?> consumerRecord : consumerRecords) {
            metric.collectConsumeMetric(consumerRecord.topic());
        }
    }

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
