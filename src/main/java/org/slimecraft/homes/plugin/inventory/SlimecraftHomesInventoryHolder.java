package org.slimecraft.homes.plugin.inventory;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.slimecraft.bedrock.inventory.BedrockInventoryHolder;
import org.slimecraft.bedrock.inventory.button.Button;
import org.slimecraft.homes.plugin.impl.model.Home;
import org.slimecraft.homes.plugin.impl.service.PlayerService;
import org.slimecraft.homes.plugin.impl.service.UserService;

import java.util.List;
import java.util.UUID;

public class SlimecraftHomesInventoryHolder extends BedrockInventoryHolder {
    public SlimecraftHomesInventoryHolder(List<Home> homes, PlayerService playerService, UserService userService) {
        super(9, Component.text("Homes"));

        final ItemStack createItemStack = ItemStack.of(Material.LIME_CONCRETE);
        createItemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("Create").color(NamedTextColor.GREEN));

        final ItemStack viewItemStack = ItemStack.of(Material.ENDER_EYE);
        viewItemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("View").color(NamedTextColor.YELLOW));

        this.addButton(new Button(0, createItemStack, (bedrockInventoryHolder, player) -> {
            final UUID identifier = player.getUniqueId();
            player.closeInventory();
            userService.setCreatingHome(identifier, true);
        }, false));
        this.addButton(new Button(4, viewItemStack, (bedrockInventoryHolder, player) -> player.openInventory(new SlimecraftHomesViewInventoryHolder(homes, playerService, 1, this).getInventory()), false));
    }

    private static final class SlimecraftHomesViewInventoryHolder extends BedrockInventoryHolder {
        public SlimecraftHomesViewInventoryHolder(List<Home> homes, PlayerService playerService, int menuPosition, BedrockInventoryHolder previous) {
            super(27, Component.text("Homes -> View -> ").append(Component.text(menuPosition).color(NamedTextColor.YELLOW)));
            final int inventorySize = this.getInventory().getSize();
            final int lastSlot = inventorySize - 1;
            final int previousButtonPosition = 18;

            final ItemStack lastPageItemStack = ItemStack.of(Material.ARROW);
            lastPageItemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("Previous Page").color(NamedTextColor.YELLOW));

            final ItemStack nextPageItemStack = ItemStack.of(Material.ARROW);
            nextPageItemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("Next Page").color(NamedTextColor.YELLOW));

            this.addButton(new Button(previousButtonPosition, lastPageItemStack, (bedrockInventoryHolder, player) -> {
                player.openInventory(previous.getInventory());
            }, false));

            for (int i = 0; i < homes.size(); i++) {
                final Home home = homes.get(i);
                if (i == previousButtonPosition) {
                    homes.remove(home);
                    homes.add(i + 1, home);
                    continue;
                }
                if (i >= lastSlot) {
                    List<Home> remainingHomes = homes.subList(i, homes.size());
                    this.addButton(new Button(lastSlot, nextPageItemStack, (bedrockInventoryHolder, player) -> player.openInventory(new SlimecraftHomesViewInventoryHolder(remainingHomes, playerService, menuPosition + 1, this).getInventory()), false));
                    break;
                }
                final Material type = home.getSafeLocation().toRegularLocation().getBlock().getType();
                final ItemStack homeItemStack = ItemStack.of(!type.isEmpty() && type.isItem() ? type : Material.BEDROCK);
                homeItemStack.setData(DataComponentTypes.ITEM_NAME, Component.text(home.getName()).color(NamedTextColor.GREEN));
                this.addButton(new Button(i, homeItemStack, (bedrockInventoryHolder, player) -> {
                    playerService.teleportPlayerToHome(player, home);
                    player.closeInventory();
                }, false));
            }
        }
    }
}
