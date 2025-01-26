package org.slimecraft.homes.plugin.impl.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.slimecraft.bedrock.model.SafeLocation;
import org.slimecraft.homes.plugin.impl.model.Home;
import org.slimecraft.homes.plugin.inventory.SlimecraftHomesInventoryHolder;

import java.util.List;
import java.util.UUID;

public class PlayerServiceImpl implements PlayerService {
    private final Plugin plugin;
    private final int teleportWaitTime;

    public PlayerServiceImpl(Plugin plugin, int teleportWaitTime) {
        this.plugin = plugin;
        this.teleportWaitTime = teleportWaitTime;
    }

    @Override
    public void sendHomeCreatedConfirmation(Player player, Home home) {
        player.sendMessage(Component.text("Created home ").append(Component.text(home.getName()).color(NamedTextColor.YELLOW)));
    }

    @Override
    public void teleportPlayerToHome(Player player, Home home) {
        Location homeLocation = home.getSafeLocation().toRegularLocation();

        homeLocation.setPitch(player.getPitch());
        homeLocation.setYaw(player.getYaw());

        new BukkitRunnable() {
            int timesRan = 0;
            final Location currentLocation = player.getLocation();

            @Override
            public void run() {
                timesRan++;

                if (this.timesRan <= teleportWaitTime) {
                    Location newLocation = player.getLocation();

                    this.currentLocation.setPitch(newLocation.getPitch());
                    this.currentLocation.setYaw(newLocation.getYaw());

                    if (!currentLocation.equals(newLocation)) {
                        player.sendMessage(Component.text("You moved! Cancelling teleport!").color(NamedTextColor.RED));
                        this.cancel();
                        return;
                    }

                    player.sendMessage(Component.text("Teleporting in: ").append(Component.text(teleportWaitTime - timesRan + 1)));
                    return;
                }

                this.cancel();
                player.teleportAsync(homeLocation);
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
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
