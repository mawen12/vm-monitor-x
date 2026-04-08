package com.github.mawen12.easeagent.core.plugins.jdbc;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class JdbcDataSourceMetricInterceptor implements NonReentrantInterceptor {

    private final AtomicInteger counter = new AtomicInteger();

    public static JdbcDataSourceMetricInterceptor INSTANCE = new JdbcDataSourceMetricInterceptor();

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {

    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Connection con = (Connection) methodInfo.getRetValue();
        String key;
        boolean success = false;

        if (methodInfo.getRetValue() == null || methodInfo.getThrowable() != null) {
            key = "err-conn";
            success = false;
        } else {
            key = getUrl(con);
        }

        counter.incrementAndGet();
        System.out.printf("jdbc.execute.cnt:%s is %d\n", key, counter.get());
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
