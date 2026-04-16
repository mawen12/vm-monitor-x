package com.github.mawen12.easeagent.api.interceptor;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.context.Context;
import lombok.AllArgsConstructor;
import lombok.Getter;

@SharedToBootstrap("used by InterceptorChain -> CommonInlineAdvice")
public interface Interceptor {

    Order order();

    default void init() {
    }

    void before(MethodInfo methodInfo, Context ctx);

    void after(MethodInfo methodInfo, Context ctx);

    @Getter
    @AllArgsConstructor
    enum Order {
        PREPARE(20),
        TRACING(100),
        METRIC(200),
        ;

        private final int order;
    }
}
