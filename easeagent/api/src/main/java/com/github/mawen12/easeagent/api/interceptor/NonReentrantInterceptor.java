package com.github.mawen12.easeagent.api.interceptor;

import com.github.mawen12.easeagent.api.context.Context;

public interface NonReentrantInterceptor extends Interceptor {

    void doBefore(MethodInfo methodInfo, Context ctx);

    void doAfter(MethodInfo methodInfo, Context ctx);

    default Object getEnterKey(MethodInfo methodInfo, Context ctx) {
        return this.getClass();
    }

    @Override
    default void before(MethodInfo methodInfo, Context ctx) {
        Object enterKey = getEnterKey(methodInfo, ctx);
        if (!ctx.enter(enterKey, 1)) {
            return;
        }
        doBefore(methodInfo, ctx);
    }

    @Override
    default void after(MethodInfo methodInfo, Context ctx) {
        Object enterKey = getEnterKey(methodInfo, ctx);
        if (!ctx.exit(enterKey, 1)) {
            return;
        }
        try {
            ctx.enter(enterKey);
            doAfter(methodInfo, ctx);
        } finally {
            ctx.exit(enterKey);
        }

    }
}
