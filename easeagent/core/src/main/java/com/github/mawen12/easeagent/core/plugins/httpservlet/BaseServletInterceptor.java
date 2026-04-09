package com.github.mawen12.easeagent.core.plugins.httpservlet;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseServletInterceptor implements NonReentrantInterceptor {

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) methodInfo.getArgs()[0];
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) methodInfo.getArgs()[0];

    }

    public abstract String getAfterMark();

    public abstract void internalAfter(String key, Throwable throwable, HttpServletRequest request, HttpServletResponse response, long start);
}
