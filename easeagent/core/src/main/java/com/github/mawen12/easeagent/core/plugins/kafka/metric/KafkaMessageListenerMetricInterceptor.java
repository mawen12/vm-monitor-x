package com.github.mawen12.easeagent.core.plugins.kafka.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.metrics.ServiceMetricRegistry;
import com.github.mawen12.easeagent.api.metrics.Tags;
import com.github.mawen12.easeagent.api.utils.ContextUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@EaseAgentClassLoader
public class KafkaMessageListenerMetricInterceptor implements NonReentrantInterceptor {
    public static final KafkaMessageListenerMetricInterceptor INSTANCE = new KafkaMessageListenerMetricInterceptor();

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
        ConsumerRecord<?, ?> consumerRecord = (ConsumerRecord<?, ?>) methodInfo.getArgs()[0];
        metric.collectConsumeMetric(consumerRecord.topic(), ContextUtils.getDuration(ctx), methodInfo.isSuccess());
    }

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
