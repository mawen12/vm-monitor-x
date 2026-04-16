package com.github.mawen12.easeagent.core.plugins.redis.trace;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;

@EaseAgentClassLoader
public class JedisTraceInterceptor extends CommonRedisTraceInterceptor{
    public static final JedisTraceInterceptor INSTANCE = new JedisTraceInterceptor();

    @Override
    public void doTraceBefore(MethodInfo methodInfo, Context ctx) {
        Object invoker = methodInfo.getInvoker();
        String name = invoker.getClass().getSimpleName() + "." + methodInfo.getMethod();
        String cmd = methodInfo.getMethod();
        this.startTrace(name, null, cmd, ctx);
    }
}
