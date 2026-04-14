package com.github.mawen12.easeagent.api.interceptor;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.context.Context;

@SharedToBootstrap("used by InterceptorChain -> CommonInlineAdvice")
public interface Interceptor {

    default int order() {
        return 0;
    }

    default void init() {
    }

    void before(MethodInfo methodInfo, Context ctx);

    void after(MethodInfo methodInfo, Context ctx);
}
