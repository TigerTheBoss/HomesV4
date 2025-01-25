package org.slimecraft.homes.plugin.impl.dao;

import org.slimecraft.bedrock.model.Dao;
import org.slimecraft.homes.plugin.impl.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SqlUserDao implements Dao<UUID, User> {
    private final DataSource dataSource;

    public SqlUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(User type) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO slimecrafthomes_user(identifier) VALUES (?)")) {
                preparedStatement.setString(1, type.getIdentifier().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> get(UUID key) {
        User user = null;
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM slimecrafthomes_user WHERE identifier = ?;")) {
                preparedStatement.setString(1, key.toString());
                try (final ResultSet results = preparedStatement.executeQuery()) {
                    while (results.next()) {
                        user = new User(key);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void update(UUID key, User type) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("")) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID key) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM slimecrafthomes_user WHERE identifier = ?;")) {
                preparedStatement.setString(1, key.toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
