package org.slimecraft.homes.plugin;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;
import org.slimecraft.bedrock.listener.BedrockInventoryHolderListener;
import org.slimecraft.bedrock.model.Dao;
import org.slimecraft.bedrock.model.MappedDao;
import org.slimecraft.homes.plugin.command.SlimecraftHomesCommand;
import org.slimecraft.homes.plugin.impl.dao.SqlHomeDao;
import org.slimecraft.homes.plugin.impl.dao.SqlUserDao;
import org.slimecraft.homes.plugin.impl.dao.SqlUserHomeMappedDao;
import org.slimecraft.homes.plugin.impl.model.Home;
import org.slimecraft.homes.plugin.impl.model.User;
import org.slimecraft.homes.plugin.impl.service.*;
import org.slimecraft.homes.plugin.listener.PlayerListener;

import java.util.UUID;

public class SlimecraftHomesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/mysql");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("123");
        final HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        final DataSourceService dataSourceService = new SqlDataSourceService(dataSource);
        dataSourceService.initialize();
        final Dao<UUID, User> userDao = new SqlUserDao(dataSource);
        final Dao<UUID, Home> homeDao = new SqlHomeDao(dataSource, new Gson());
        final MappedDao<UUID, Home> userHomeMappedDao = new SqlUserHomeMappedDao(dataSource);
        final UserService userService = new UserServiceImpl(userDao, homeDao, userHomeMappedDao);
        final PlayerService playerService = new PlayerServiceImpl();
        final PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this);

        new SlimecraftHomesCommand(userService, playerService, this, this.getServer().getScheduler()).registerCommand(commandManager);
        this.getServer().getPluginManager().registerEvents(new BedrockInventoryHolderListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(userService), this);
    }
}
