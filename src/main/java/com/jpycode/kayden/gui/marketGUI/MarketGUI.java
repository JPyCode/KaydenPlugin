package com.jpycode.kayden.gui.marketGUI;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class MarketGUI implements Listener {
    private final Plugin plugin;
    public MarketGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    public void openGUI(Player player) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, Component.text("Market"));


        player.openInventory(inventory);
    }
}
