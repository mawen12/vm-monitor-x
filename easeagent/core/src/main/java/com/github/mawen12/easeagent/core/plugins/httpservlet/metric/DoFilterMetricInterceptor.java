package com.github.mawen12.easeagent.core.plugins.httpservlet.metric;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.metrics.ServerMetric;
import com.github.mawen12.easeagent.api.metrics.ServiceMetricRegistry;
import com.github.mawen12.easeagent.core.plugins.httpservlet.BaseServletInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoFilterMetricInterceptor extends BaseServletInterceptor {
    private static final String AFTER_MARK = DoFilterMetricInterceptor.class.getName() + "$AfterMark";

    private ServerMetric metric;

    @Override
    public void init() {
        metric = ServiceMetricRegistry.getOrCreate("application", "http-request", "url", ServerMetric::new);
    }

    @Override
    public String getAfterMark() {
        return AFTER_MARK;
    }

    @Override
    public void internalAfter(String key, Throwable throwable, HttpServletRequest request, HttpServletResponse response, long start) {
        long end = System.currentTimeMillis();
        metric.collectMetric(key, response.getStatus(), throwable, start, end);
    }
}
