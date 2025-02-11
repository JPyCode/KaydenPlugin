package com.jpycode.kayden.gui.marketGUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class MarketMenu {


    public void openGUI(Player player) {

        Inventory gui = Bukkit.createInventory(null, 9 * 5, "Market Menu");


        player.openInventory(gui);


    }

}
