package com.github.mawen12.easeagent.core.plugins.jdbc;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.field.DynamicFieldAccessor;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.core.plugins.jdbc.common.SqlInfo;

import java.sql.Statement;

public enum JdbcStmtPrepareInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        System.out.println("[agent] doBefore Execute the JdbcStmtPrepareInterceptor");
        Statement stmt = (Statement) methodInfo.getInvoker();
        if (!(stmt instanceof DynamicFieldAccessor)) {
            System.err.printf("%s must implements %s\n", stmt.getClass().getCanonicalName(), DynamicFieldAccessor.class.getName());
            return;
        }

        SqlInfo sqlInfo = DynamicFieldAccessor.getDynamicFieldValue(stmt);
        if (sqlInfo == null) {
            return;
        }

        String sql = null;
        if (methodInfo.getArgs() != null && methodInfo.getArgs().length > 0) {
            sql = (String) methodInfo.getArgs()[0];
        }

        String method = methodInfo.getMethod();
        if (method.equals("addBatch") && sql != null) {
            sqlInfo.addSql(sql, true);
        } else if (method.equals("clearBatch")) {
            sqlInfo.clearSql();
        } else if (method.startsWith("execute") && sql != null) {
            sqlInfo.addSql(sql, false);
        }

        ctx.put(SqlInfo.class, sqlInfo);
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public int order() {
        return 20;
    }
}
