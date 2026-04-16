package com.github.mawen12.easeagent.core.plugins.redis.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.core.plugins.redis.CommonRedisInterceptor;

@EaseAgentClassLoader
public class JedisMetricInterceptor extends CommonRedisInterceptor {
    public static JedisMetricInterceptor INSTANCE = new JedisMetricInterceptor();

    @Override
    public String getKey(MethodInfo methodInfo, Context ctx) {
        return methodInfo.getMethod();
    }
}
