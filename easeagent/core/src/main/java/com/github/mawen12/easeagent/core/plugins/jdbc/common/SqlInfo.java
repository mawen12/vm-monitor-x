package com.github.mawen12.easeagent.core.plugins.jdbc.common;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import lombok.Getter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@EaseAgentClassLoader
public class SqlInfo {
    @Getter
    private final Connection connection;

    private final List<String> sqlList = new ArrayList<>();

    public SqlInfo(Connection connection) {
        this.connection = connection;
    }

    public void addSql(String sql, boolean forBatch) {
        if (forBatch) {
            this.sqlList.clear();
        }
        this.sqlList.add(sql);
    }

    public void clearSql() {
        this.sqlList.clear();
    }

    public String getSql() {
        if (this.sqlList.isEmpty()) {
            return null;
        }
        return String.join("\n", sqlList);
    }

    @Override
    public String toString() {
        return "SqlInfo{" +
                "connection=" + connection +
                ", sqlList=" + sqlList +
                '}';
    }
}
