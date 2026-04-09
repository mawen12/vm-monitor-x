package com.github.mawen12.easeagent.core.plugins.jdbc.metric;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.metrics.ServiceMetricRegistry;
import com.github.mawen12.easeagent.core.plugins.jdbc.JdbcMetric;
import com.github.mawen12.easeagent.core.plugins.jdbc.common.SqlInfo;

public class JdbcStmtMetricInterceptor implements NonReentrantInterceptor {
    public static final JdbcStmtMetricInterceptor INSTANCE = new JdbcStmtMetricInterceptor();

    private JdbcMetric metric;

    @Override
    public void init() {
        metric = ServiceMetricRegistry.getOrCreate("application", "jdbc-statement", "signature",
                metricRegistry -> new JdbcMetric(metricRegistry, JdbcMetric.nameFactory()));
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        SqlInfo sqlInfo = ctx.get(SqlInfo.class);
        String sql = sqlInfo.getSql();
        metric.collectMetric(sql, methodInfo.getThrowable() == null, ctx);
    }

    @Override
    public int order() {
        return 200;
    }
}
