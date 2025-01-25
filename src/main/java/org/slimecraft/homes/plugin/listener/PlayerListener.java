package org.slimecraft.homes.plugin.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.slimecraft.homes.plugin.impl.model.Home;
import org.slimecraft.homes.plugin.impl.service.PlayerService;
import org.slimecraft.homes.plugin.impl.service.UserService;

import java.util.UUID;

public class PlayerListener implements Listener {
    private final UserService userService;
    private final PlayerService playerService;
    private final BukkitScheduler bukkitScheduler;
    private final Plugin plugin;

    public PlayerListener(UserService userService, PlayerService playerService, BukkitScheduler bukkitScheduler, Plugin plugin) {
        this.userService = userService;
        this.playerService = playerService;
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID identifier = player.getUniqueId();

        this.userService.exists(identifier).thenAccept(exists -> {
            if (exists) return;
            this.userService.createNewUser(identifier);
        });
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final UUID identifier = player.getUniqueId();
        final boolean creatingHome = this.userService.isCreatingHome(identifier).join();

        if (!creatingHome) return;
        event.setCancelled(true);
        final Home home = this.playerService.getHomeFromLocation(player, UUID.randomUUID(), PlainTextComponentSerializer.plainText().serialize(event.message()));
        this.userService.addHome(identifier, home);
        this.userService.setCreatingHome(identifier, false);
        this.playerService.sendHomeCreatedConfirmation(player, home);
    }
}
