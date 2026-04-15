package com.github.mawen12.easeagent.core.plugins.jdbc.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseInfo {
    private String databaseType;
    private String database;
    private String host;
    private int port;

    public static DatabaseInfo getFromConnection(Connection connection) {
        try {
            String url = connection.getMetaData().getURL();
            URI uri = URI.create(url.substring(5));
            String databaseType = uri.getScheme();
            String database = connection.getCatalog();
            String host = uri.getHost() == null ? "" : uri.getHost();
            int port = uri.getPort() == -1 ? 3306 : uri.getPort();

            return new DatabaseInfo(databaseType, database, host, port);
        } catch (SQLException ignored) {

        }
        return null;
    }
}
