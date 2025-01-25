package org.slimecraft.homes.plugin.impl.service;

import org.bukkit.entity.Player;
import org.slimecraft.homes.plugin.impl.model.Home;

import java.util.List;
import java.util.UUID;

public interface PlayerService {
    void teleportPlayerToHome(Player player, Home home);

    void showHomesInventory(Player player, List<Home> homes);

    void sendHomeList(Player player, List<Home> homes);

    Home getHomeFromLocation(Player player, UUID identifier, String name);
}
