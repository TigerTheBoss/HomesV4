package org.slimecraft.homes.plugin.impl.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDataSourceService implements DataSourceService {
    private final DataSource dataSource;

    public SqlDataSourceService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void initialize() {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final Statement statement = connection.createStatement()) {
                statement.addBatch("CREATE TABLE IF NOT EXISTS slimecrafthomes_user(identifier CHAR(36) PRIMARY KEY, creatingHome BOOLEAN);");
                statement.addBatch("CREATE TABLE IF NOT EXISTS slimecrafthomes_home(identifier CHAR(36) PRIMARY KEY, name VARCHAR(200), safeLocation VARCHAR(300));");
                statement.addBatch("CREATE TABLE IF NOT EXISTS slimecrafthomes_user_homes(user CHAR(36), home CHAR(36), FOREIGN KEY (user) REFERENCES slimecrafthomes_user (identifier), FOREIGN KEY (home) REFERENCES slimecrafthomes_home (identifier));");
                statement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
