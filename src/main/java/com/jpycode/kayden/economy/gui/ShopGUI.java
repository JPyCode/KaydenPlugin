package com.jpycode.kayden.economy.gui;

import com.jpycode.kayden.economy.utils.ShopItemLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ShopGUI {
    public static Inventory createShopGUI() {
        File file = new File(Bukkit.getPluginManager().getPlugin("economy").getDataFolder(), "shop.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains("shop.weapons")) return null;

        String shopName = ChatColor.translateAlternateColorCodes('&', config.getString("shop.weapons.name"));
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + shopName);

        int slot = 0;
        for (String key : config.getConfigurationSection("shop.weapons.items").getKeys(false)) {
            ItemStack item = ShopItemLoader.createItem(config, "shop.weapons.items." + key);
            inv.setItem(slot++, item);
        }

        return inv;
    }
}
