package org.slimecraft.homes.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.slimecraft.homes.plugin.impl.service.UserService;

import java.util.UUID;

public class PlayerListener implements Listener {
    private final UserService userService;

    public PlayerListener(UserService userService) {
        this.userService = userService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID identifier = player.getUniqueId();

        this.userService.userExists(identifier).thenAccept(aBoolean -> {
            if (aBoolean) return;
            this.userService.createNewUser(identifier);
        });
    }
}
