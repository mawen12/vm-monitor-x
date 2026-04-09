package com.github.mawen12.easeagent.core.plugins.jdbc.metric;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.core.plugins.jdbc.JdbcMetric;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class JdbcDataSourceMetricInterceptor implements NonReentrantInterceptor {
    public static JdbcDataSourceMetricInterceptor INSTANCE = new JdbcDataSourceMetricInterceptor();

    private JdbcMetric metric;

    @Override
    public void init() {
        metric = JdbcMetric.init(Agent.getMetricRegistry(), JdbcMetric.nameFactory());
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Connection con = (Connection) methodInfo.getRetValue();
        String key;
        boolean success = true;

        if (methodInfo.getRetValue() == null || methodInfo.getThrowable() != null) {
            key = "err-conn";
            success = false;
        } else {
            key = getUrl(con);
        }

        metric.collectMetric(key, success, ctx);
    }

    private static String getUrl(Connection conn) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            String url = meta.getURL();
            int idx = url.indexOf("?");
            if (idx == -1) {
                return url;
            }
            return url.substring(0, idx);
        } catch (SQLException ignored) {

        }
        return null;
    }
}
