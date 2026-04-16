package com.github.mawen12.easeagent.core.plugins.kafka.metric;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.metrics.ServiceMetricRegistry;
import com.github.mawen12.easeagent.api.metrics.Tags;
import com.github.mawen12.easeagent.core.plugins.kafka.common.AsyncCallback;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;

@SharedToBootstrap
public class KafkaProducerMetricInterceptor implements NonReentrantInterceptor {
    public static final KafkaProducerMetricInterceptor INSTANCE = new KafkaProducerMetricInterceptor();

    private KafkaMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "kafka", "resource");
        metric = ServiceMetricRegistry.getOrCreate(tags, KafkaMetric.KafkaNameFactory.INSTANCE, KafkaMetric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        ProducerRecord record = (ProducerRecord) methodInfo.getArgs()[0];
        MetricCallback callback = new MetricCallback(AsyncCallback.callback(methodInfo), record.topic(), metric);
        methodInfo.changeArg(1, callback);
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Callback callback = AsyncCallback.callback(methodInfo);
        if (AsyncCallback.isAsync(callback)) {
            return;
        }
        if (callback instanceof MetricCallback) {
            MetricCallback metricCallback = (MetricCallback) callback;
            metric.collectProducerMetric(metricCallback.getTopic(), System.currentTimeMillis() - metricCallback.getStart(), methodInfo.isSuccess());
        }
    }

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
