package com.jpycode.kayden.economy.listeners.shop;

import com.jpycode.kayden.economy.utils.ShopItemLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

import static com.jpycode.kayden.economy.database.BalanceAsync.getBalance;
import static com.jpycode.kayden.economy.database.BalanceAsync.setBalance;


public class ShopInventoryListener implements Listener {
    static File file = new File(Bukkit.getPluginManager().getPlugin("Economy").getDataFolder(), "shop.yml");
    static FileConfiguration shopConfig = YamlConfiguration.loadConfiguration(file);

    public static void ShopInventoryClick(Player player, ItemStack clickedItem, InventoryClickEvent event) {
        event.setCancelled(true);

        if (clickedItem == null || !clickedItem.hasItemMeta()) return;
        if (!shopConfig.contains("shop.weapons.items")) return;


        for (String key : shopConfig.getConfigurationSection("shop.weapons.items").getKeys(false)) {
            ItemStack shopItem = ShopItemLoader.createItem(shopConfig, "shop.weapons.items." + key);
            if (clickedItem.isSimilar(shopItem)) {
                double price = shopConfig.getDouble("shop.weapons.items." + key + ".price");

                getBalance(player).thenAccept(balance -> {
                    if (balance < price) {
                        player.sendMessage(ChatColor.RED + "Saldo insuficiente para comprar: " + clickedItem.getItemMeta().getDisplayName());
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        return;
                    }

                    setBalance(player, balance - price);
                    player.sendMessage(ChatColor.GREEN + "Você comprou " + clickedItem.getItemMeta().getDisplayName() + " por R$" + price);
                    player.getInventory().addItem(clickedItem.clone());
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
                });

                break;
            }
        }
    }
}
