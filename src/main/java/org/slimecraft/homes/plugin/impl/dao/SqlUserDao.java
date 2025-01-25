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
            try (final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO slimecrafthomes_user(identifier, creatingHome) VALUES (?, ?)")) {
                preparedStatement.setString(1, type.getIdentifier().toString());
                preparedStatement.setBoolean(2, type.isCreatingHome());
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
                        final boolean creatingHome = results.getBoolean("creatingHome");
                        user = new User(key, null, creatingHome);
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
            try (final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE slimecrafthomes_user SET creatingHome = ? WHERE identifier = ?")) {
                preparedStatement.setBoolean(1, type.isCreatingHome());
                preparedStatement.setString(2, key.toString());
                preparedStatement.executeUpdate();
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
