package com.github.mawen12.easeagent.core.plugins.httpservlet;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.core.plugins.httpservlet.common.InternalAsyncListener;
import com.github.mawen12.easeagent.core.plugins.httpservlet.common.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EaseAgentClassLoader
public abstract class BaseServletInterceptor implements NonReentrantInterceptor {

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        HttpServletRequest request = (HttpServletRequest) methodInfo.getArgs()[0];
        ServletUtils.startTime(request);
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        HttpServletRequest request = (HttpServletRequest) methodInfo.getArgs()[0];
        if (ServletUtils.markProcessed(request, getAfterMark())) {
            return;
        }

        long start = ServletUtils.startTime(request);
        String key = request.getMethod() + " " + ServletUtils.getHttpRoute(request);
        HttpServletResponse response = (HttpServletResponse) methodInfo.getArgs()[1];

        if (request.isAsyncStarted()) {
            request.getAsyncContext().addListener(new InternalAsyncListener(
                    asyncEvent -> {
                        HttpServletResponse suppliedResponse = (HttpServletResponse) asyncEvent.getSuppliedResponse();
                        internalAfter(key, asyncEvent.getThrowable(), request, suppliedResponse, start);
                    }
            ));
        } else {
            internalAfter(key, methodInfo.getThrowable(), request, response, start);
        }
    }

    public abstract String getAfterMark();

    public abstract void internalAfter(String key, Throwable throwable, HttpServletRequest request, HttpServletResponse response, long start);
}
