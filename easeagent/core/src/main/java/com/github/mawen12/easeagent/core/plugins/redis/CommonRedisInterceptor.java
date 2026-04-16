package com.github.mawen12.easeagent.core.plugins.redis;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.logging.Logger;
import com.github.mawen12.easeagent.api.metrics.ServiceMetricRegistry;
import com.github.mawen12.easeagent.api.metrics.Tags;
import com.github.mawen12.easeagent.api.utils.ContextUtils;
import com.github.mawen12.easeagent.core.plugins.redis.metric.RedisMetric;

@EaseAgentClassLoader
public abstract class CommonRedisInterceptor implements NonReentrantInterceptor {
    private static final Logger LOGGER = Agent.getLogger(CommonRedisInterceptor.class);
    private static RedisMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "cache-redis", "signature");
        metric = ServiceMetricRegistry.getOrCreate(tags, RedisMetric.RedisNameFactorySupplier.INSTANCE, RedisMetric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        LOGGER.info("into redis metric");
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        LOGGER.info("out redis metric");
        String key = getKey(methodInfo, ctx);
//        LOGGER.info("redis key is {}", key);
//        LOGGER.info("after class<{}>.{}({})", methodInfo.getInvoker(), methodInfo.getMethod(), Arrays.toString(methodInfo.getArgs()));
        metric.collect(key, ContextUtils.getDuration(ctx), methodInfo.isSuccess());
    }

    public abstract String getKey(MethodInfo methodInfo, Context ctx);

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
