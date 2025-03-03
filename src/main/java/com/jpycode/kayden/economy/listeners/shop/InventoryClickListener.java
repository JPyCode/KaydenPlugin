package com.jpycode.kayden.economy.listeners.shop;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.jpycode.kayden.economy.listeners.shop.ShopInventoryListener.ShopInventoryClick;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        String title = ChatColor.stripColor(event.getView().getTitle());
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (title.equalsIgnoreCase("loja de armamentos")) {
            ShopInventoryClick(player, clickedItem, event);
        }
    }
}
