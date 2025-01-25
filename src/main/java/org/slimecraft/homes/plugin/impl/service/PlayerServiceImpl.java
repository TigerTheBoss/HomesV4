package org.slimecraft.homes.plugin.impl.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.slimecraft.bedrock.model.SafeLocation;
import org.slimecraft.homes.plugin.impl.model.Home;
import org.slimecraft.homes.plugin.inventory.SlimecraftHomesInventoryHolder;

import java.util.List;
import java.util.UUID;

public class PlayerServiceImpl implements PlayerService {

    @Override
    public void sendHomeCreatedConfirmation(Player player, Home home) {
        player.sendMessage(Component.text("Created home ").append(Component.text(home.getName()).color(NamedTextColor.YELLOW)));
    }

    @Override
    public void teleportPlayerToHome(Player player, Home home) {
        Location homeLocation = home.getSafeLocation().toRegularLocation();

        homeLocation.setPitch(player.getPitch());
        homeLocation.setYaw(player.getYaw());

        player.teleport(homeLocation);
    }

    @Override
    public void showHomesInventory(Player player, List<Home> homes, UserService userService) {
        player.openInventory(new SlimecraftHomesInventoryHolder(homes, this, userService).getInventory());
    }

    @Override
    public void sendHomeList(Player player, List<Home> homes) {
        for (int i = 0; i < homes.size(); i++) {
            final Home home = homes.get(i);
            player.sendMessage(Component.text(i + 1).append(Component.text(": ").append(Component.text(home.getName()).color(NamedTextColor.YELLOW))));
        }
    }

    @Override
    public Home getHomeFromLocation(Player player, UUID identifier, String name) {
        return new Home(identifier, name, new SafeLocation(player.getWorld().getUID(), player.getX(), player.getY(), player.getZ(), player.getPitch(), player.getYaw()));
    }
}
