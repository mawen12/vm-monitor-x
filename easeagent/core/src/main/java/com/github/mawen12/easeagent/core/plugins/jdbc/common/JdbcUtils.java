package com.github.mawen12.easeagent.core.plugins.jdbc.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class JdbcUtils {

    public static String getUrl(Connection conn) {
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
