package org.slimecraft.homes.plugin.impl.dao;

import com.google.gson.Gson;
import org.slimecraft.bedrock.model.Dao;
import org.slimecraft.bedrock.model.SafeLocation;
import org.slimecraft.homes.plugin.impl.model.Home;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SqlHomeDao implements Dao<UUID, Home> {
    private final DataSource dataSource;
    private final Gson gson;

    public SqlHomeDao(DataSource dataSource, Gson gson) {
        this.dataSource = dataSource;
        this.gson = gson;
    }

    @Override
    public void create(Home type) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO slimecrafthomes_home (identifier, name, safeLocation) VALUES (?, ?, ?);")) {
                preparedStatement.setString(1, type.getIdentifier().toString());
                preparedStatement.setString(2, type.getName());
                preparedStatement.setString(3, this.gson.toJson(type.getSafeLocation()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Home> get(UUID key) {
        Home home = null;
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM slimecrafthomes_home WHERE identifier = ?;")) {
                preparedStatement.setString(1, key.toString());
                final ResultSet results = preparedStatement.executeQuery();
                while (results.next()) {
                    final String name = results.getString("name");
                    final SafeLocation safeLocation = this.gson.fromJson(results.getString("safeLocation"), SafeLocation.class);
                    home = new Home(key, name, safeLocation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(home);
    }

    @Override
    public void update(UUID key, Home type) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE slimecrafthomes_home SET name = ?, safeLocation = ? WHERE identifier = ?;")) {
                preparedStatement.setString(1, type.getName());
                preparedStatement.setString(2, this.gson.toJson(type.getSafeLocation()));
                preparedStatement.setString(3, key.toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID key) {
        try (final Connection connection = this.dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM slimecrafthomes_home WHERE identifier = ?;")) {
                preparedStatement.setString(1, key.toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
