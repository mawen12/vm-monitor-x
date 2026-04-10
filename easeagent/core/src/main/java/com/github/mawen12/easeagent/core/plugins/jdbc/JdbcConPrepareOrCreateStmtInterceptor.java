package com.github.mawen12.easeagent.core.plugins.jdbc;

import com.github.mawen12.easeagent.api.context.Context;
import com.github.mawen12.easeagent.api.field.DynamicFieldAccessor;
import com.github.mawen12.easeagent.api.interceptor.MethodInfo;
import com.github.mawen12.easeagent.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.easeagent.core.plugins.jdbc.common.SqlInfo;

import java.sql.Connection;
import java.sql.Statement;

public enum JdbcConPrepareOrCreateStmtInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Statement stmt = (Statement) methodInfo.getRetValue();
        SqlInfo sqlInfo = new SqlInfo((Connection) methodInfo.getInvoker());
        if (methodInfo.getMethod().startsWith("prepare") && methodInfo.getArgs() != null && methodInfo.getArgs().length > 0) {
            String sql = (String) methodInfo.getArgs()[0];
            if (sql != null) {
                sqlInfo.addSql(sql, false);
            }
        }

        if (stmt instanceof DynamicFieldAccessor) {
            DynamicFieldAccessor.setDynamicFieldValue(stmt, sqlInfo);
        } else {
            System.err.printf("%s must implements %s\n", stmt.getClass().getCanonicalName(), DynamicFieldAccessor.class.getName());
        }
    }
}
