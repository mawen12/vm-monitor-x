package com.github.mawen12.easeagent.core.plugins.httpservlet.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.ServerMetric;
import com.github.mawen12.easeagent.api.metrics.ServiceMetricRegistry;
import com.github.mawen12.easeagent.api.metrics.Tags;
import com.github.mawen12.easeagent.core.plugins.httpservlet.BaseServletInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EaseAgentClassLoader
public class DoFilterMetricInterceptor extends BaseServletInterceptor {
    public static final DoFilterMetricInterceptor INSTANCE = new DoFilterMetricInterceptor();

    private static final String AFTER_MARK = DoFilterMetricInterceptor.class.getName() + "$AfterMark";

    private ServerMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("application", "http-request", "url");
        metric = ServiceMetricRegistry.getOrCreate(tags, ServerMetric.NAME_FACTORY, ServerMetric::new);
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
