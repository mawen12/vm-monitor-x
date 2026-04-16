package com.github.mawen12.easeagent.core.plugins.jdbc.trace;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.api.logging.Logger;
import com.github.mawen12.easeagent.api.trace.Span;
import com.github.mawen12.easeagent.core.plugins.jdbc.common.DatabaseInfo;
import com.github.mawen12.easeagent.core.plugins.jdbc.common.JdbcUtils;
import com.github.mawen12.easeagent.core.plugins.jdbc.common.SqlInfo;

import java.sql.Connection;

@EaseAgentClassLoader
public enum JdbcStmtTracingInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    private final static Logger LOGGER = Agent.getLogger(JdbcStmtTracingInterceptor.class);
    private final static String SPAN_KEY = JdbcStmtTracingInterceptor.class.getName() + "-SPAN";

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        SqlInfo sqlInfo = ctx.get(SqlInfo.class);
        if (sqlInfo == null) {
            LOGGER.warn("must get sqlInfo from context");
            return;
        }

        Span span = ctx.nextSpan();
        span.name(methodInfo.getMethod())
                .kind(Span.Kind.CLIENT)
                .tag("sql", sqlInfo.getSql())
                .tag("local-component", "database")
                .tag("component.type", "database");

        Connection connection = sqlInfo.getConnection();
        String url = JdbcUtils.getUrl(connection);
        if (url != null) {
            span.tag("url", url);
        }

        DatabaseInfo databaseInfo = DatabaseInfo.getFromConnection(connection);
        if (databaseInfo != null) {
            span.remoteServiceName(databaseInfo.getDatabaseType() + "-" + databaseInfo.getDatabase());
            span.remoteIpAndPort(databaseInfo.getHost(), databaseInfo.getPort());
        }

        span.start();
        ctx.put(SPAN_KEY, span);
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Span span = ctx.get(SPAN_KEY);
        if (methodInfo.getThrowable() != null) {
            span.error(methodInfo.getThrowable());
        }
        span.finish();
    }

    @Override
    public Order order() {
        return Order.TRACING;
    }
}
