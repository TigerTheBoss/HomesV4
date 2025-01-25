package org.slimecraft.homes.plugin.impl.dao;

import org.slimecraft.bedrock.model.MappedDao;
import org.slimecraft.homes.plugin.impl.model.Home;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SqlUserHomeMappedDao implements MappedDao<UUID, Home> {
    private final DataSource dataSource;

    public SqlUserHomeMappedDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(UUID key, Home type) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO slimecrafthomes_user_homes (user, home) VALUES (?, ?);")) {
                preparedStatement.setString(1, key.toString());
                preparedStatement.setString(2, type.getIdentifier().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Home> get(UUID key) {
        List<Home> homes = new ArrayList<>();
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM slimecrafthomes_user_homes WHERE user = ?;")) {
                preparedStatement.setString(1, key.toString());
                final ResultSet results = preparedStatement.executeQuery();
                while (results.next()) {
                    final Home home = new Home(UUID.fromString(results.getString("home")));
                    homes.add(home);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }

    @Override
    public void delete(UUID key, UUID otherKey) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM slimecrafthomes_user_homes WHERE user = ? AND home = ?;")) {
                preparedStatement.setString(1, key.toString());
                preparedStatement.setString(2, otherKey.toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
