package com.github.mawen12.easeagent.core.plugins.demo;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.Interceptor;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;

@EaseAgentClassLoader
@SharedToBootstrap("used by InterceptorChain -> CommonInlineAdvice")
public enum DemoInterceptor implements Interceptor {
    INSTANCE;

    @Override
    public void before(MethodInfo methodInfo, Context ctx) {
        System.out.println("[agent] before the " + methodInfo.getMethod());
    }

    @Override
    public void after(MethodInfo methodInfo, Context ctx) {
        System.out.println("[agent] after the " + methodInfo.getMethod());
    }
}
