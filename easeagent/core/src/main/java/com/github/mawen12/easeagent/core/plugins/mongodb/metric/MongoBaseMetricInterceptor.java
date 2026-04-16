package com.github.mawen12.easeagent.core.plugins.mongodb.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.core.plugins.mongodb.common.MongoBaseInterceptor;

@EaseAgentClassLoader
public class MongoBaseMetricInterceptor extends MongoBaseInterceptor {

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
