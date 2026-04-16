package com.github.mawen12.easeagent.core.plugins.redis.trace;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.logging.Logger;
import com.github.mawen12.easeagent.api.trace.Span;

public abstract class CommonRedisTraceInterceptor implements NonReentrantInterceptor {
    private static final Logger LOGGER = Agent.getLogger(CommonRedisTraceInterceptor.class);

    private static final Object SPAN_KEY = new Object();

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        LOGGER.info("into redis trace");
        Span span = ctx.currentSpan();
        if (span.isNoop()) {
            return;
        }

        doTraceBefore(methodInfo, ctx);
    }

    public abstract void doTraceBefore(MethodInfo methodInfo, Context ctx);

    protected void startTrace(String name, String uri, String cmd, Context ctx) {
        Span span = ctx.nextSpan().name(name).start()
                .kind(Span.Kind.CLIENT)
                .remoteServiceName("redis")
                .tag("component.type", "redis");

        if (cmd != null) {
            span.tag("redis.method", cmd);
        }
        ctx.put(SPAN_KEY, span);
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        LOGGER.info("out lettuce trace");
        try {
            Span span = ctx.get(SPAN_KEY);
            if (span == null) {
                return;
            }

            if (methodInfo.getThrowable() != null) {
                span.error(methodInfo.getThrowable());
            }

            span.finish();
            ctx.remove(SPAN_KEY);
        } catch (Exception ignored) {

        }
    }

    @Override
    public Order order() {
        return Order.TRACING;
    }
}
