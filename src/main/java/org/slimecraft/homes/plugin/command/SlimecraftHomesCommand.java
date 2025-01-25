package org.slimecraft.homes.plugin.command;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.meta.CommandMeta;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;
import org.slimecraft.homes.plugin.impl.model.Home;
import org.slimecraft.homes.plugin.impl.service.PlayerService;
import org.slimecraft.homes.plugin.impl.service.UserService;

import java.util.UUID;

public class SlimecraftHomesCommand {
    private final UserService userService;
    private final PlayerService playerService;
    private final Plugin plugin;
    private final BukkitScheduler bukkitScheduler;

    public SlimecraftHomesCommand(UserService userService, PlayerService playerService, Plugin plugin, BukkitScheduler bukkitScheduler) {
        this.userService = userService;
        this.playerService = playerService;
        this.plugin = plugin;
        this.bukkitScheduler = bukkitScheduler;
    }

    public void registerCommand(CommandManager<Source> commandManager) {
        final CloudKey<String> NAME_KEY = CloudKey.of("name", String.class);
        final Command.Builder<Source> builder = Command.newBuilder("slimecrafthomes", CommandMeta.empty());
        final Command.Builder<PlayerSource> create = builder
                .senderType(PlayerSource.class)
                .literal("create")
                .required(NAME_KEY, StringParser.stringParser())
                .handler(commandContext -> {
                    final String name = commandContext.get(NAME_KEY);
                    final Player player = commandContext.sender().source();
                    final UUID identifier = player.getUniqueId();

                    this.userService.addHome(identifier, this.playerService.getHomeFromLocation(player, UUID.randomUUID(), name));
                });
        final Command.Builder<PlayerSource> list = builder
                .senderType(PlayerSource.class)
                .literal("list")
                .handler(commandContext -> {
                    final Player player = commandContext.sender().source();
                    final UUID identifier = player.getUniqueId();

                    this.userService.getHomes(identifier).thenAcceptAsync(homes -> {
                       this.playerService.sendHomeList(player, homes);
                    });
                });
        final Command.Builder<PlayerSource> delete = builder
                .senderType(PlayerSource.class)
                .literal("delete")
                .required(NAME_KEY, StringParser.stringParser())
                .handler(commandContext -> {
                    final String name = commandContext.get(NAME_KEY);
                    final Player player = commandContext.sender().source();
                    final UUID identifier = player.getUniqueId();

                    this.userService.getHomes(identifier).thenAcceptAsync(homes -> {
                        for (Home home : homes) {
                            if (home.getName().equalsIgnoreCase(name)) {
                                this.userService.removeHome(identifier, home.getIdentifier());
                            }
                        }
                    });
                });
        final Command.Builder<PlayerSource> view = builder
                .senderType(PlayerSource.class)
                .literal("view")
                .handler(commandContext -> {
                    final Player player = commandContext.sender().source();
                    this.userService.getHomes(player.getUniqueId()).thenAcceptAsync(homes -> this.bukkitScheduler.runTask(this.plugin, () -> this.playerService.showHomesInventory(player, homes)));
                });

        commandManager.command(create);
        commandManager.command(list);
        commandManager.command(delete);
        commandManager.command(view);
    }
}
