package com.github.mawen12.easeagent.core.plugins.springboot.state;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;

public enum SpringBootReadyInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Object event = methodInfo.getArgs()[0];
        if (event.getClass().getCanonicalName().equals("org.springframework.boot.context.event.ApplicationReadyEvent")) {
            System.out.println("SpringBoot is ready, mark SpringBootReady state");
            Agent.markSpringBootReady();
        }
        System.out.println("SpringBootReadyInterceptor doAfter, event: " + event);
    }
}
