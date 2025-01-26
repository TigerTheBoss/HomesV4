package org.slimecraft.homes.plugin;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
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
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.util.UUID;

public class SlimecraftHomesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/slimecrafthomes");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("123");
        final HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        final DataSourceService dataSourceService = new SqlDataSourceService(dataSource);
        dataSourceService.initialize();
        final Dao<UUID, User> userDao = new SqlUserDao(dataSource);
        final Dao<UUID, Home> homeDao = new SqlHomeDao(dataSource, new Gson());
        final MappedDao<UUID, Home> userHomeMappedDao = new SqlUserHomeMappedDao(dataSource);
        final UserService userService = new UserServiceImpl(userDao, homeDao, userHomeMappedDao);
        this.saveResource("config.yml", false);
        final YamlConfigurationLoader configurationLoader = YamlConfigurationLoader.builder()
                .path(this.getDataPath().resolve("config.yml"))
                .build();
        final ConfigurationService configurationService = new ConfigurationServiceImpl(configurationLoader);
        final BukkitScheduler bukkitScheduler = this.getServer().getScheduler();
        final PlayerService playerService = new PlayerServiceImpl(this, configurationService.getTeleportWaitTime());
        final PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this);
        new SlimecraftHomesCommand(userService, playerService, this, bukkitScheduler).registerCommand(commandManager);
        this.getServer().getPluginManager().registerEvents(new BedrockInventoryHolderListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(userService, playerService, bukkitScheduler, this), this);
    }
}
