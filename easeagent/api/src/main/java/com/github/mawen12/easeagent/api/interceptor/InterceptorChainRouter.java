package com.github.mawen12.easeagent.api.interceptor;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SharedToBootstrap("used by CommonInlineAdvice")
public class InterceptorChainRouter {

    public static final InterceptorChainRouter INSTANCE = new InterceptorChainRouter();

    private final ConcurrentMap<String, InterceptorChain> byMethodName = new ConcurrentHashMap<>();

    public void add(String methodName, InterceptorChain interceptorChain) {
        if (byMethodName.containsKey(methodName)) {
            throw new IllegalStateException("[agent] method " + methodName + " already exists");
        }
        byMethodName.put(methodName, interceptorChain);
    }

    public InterceptorChain resolve(String methodName) {
        return byMethodName.get(methodName);
    }
}
