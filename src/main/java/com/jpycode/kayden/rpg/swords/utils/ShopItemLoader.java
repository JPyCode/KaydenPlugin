package com.jpycode.kayden.rpg.swords.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopItemLoader {
    public static ItemStack createItem(FileConfiguration config, String path) {
        Material material = Material.valueOf(config.getString(path + ".item"));
        int amount = config.getInt(path + ".amount", 1);
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Define o nome customizado do item

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(path + ".name")));


            // Define a lore do item

            List<String> lore = config.getStringList(path + ".lore");
            List<String> formattedLore = new ArrayList<>();
            for (String line : lore) {
                formattedLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }


            double damage = config.getDouble(path + ".damage");
//            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier("attack_damage", damage, AttributeModifier.Operation.ADD_NUMBER));
            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                    new AttributeModifier(
                            new NamespacedKey("economy", "attack_damage"),
                            damage,
                            AttributeModifier.Operation.ADD_NUMBER));

            double price = config.getDouble(path + ".price");
            formattedLore.add(ChatColor.LIGHT_PURPLE + "Dano: " + damage);
            formattedLore.add(ChatColor.GOLD + "Preço: " + ChatColor.YELLOW + price);
            meta.setLore(formattedLore);


            // Adiciona encantamentos
            if (config.contains(path + ".enchants")) {
                for (String ench : config.getConfigurationSection(path + ".enchants").getKeys(false)) {
                    Enchantment enchantment = Enchantment.getByName(ench);
                    int level = config.getInt(path + ".enchants." + ench);
                    if (enchantment != null) {
                        meta.addEnchant(enchantment, level, true);
                    }
                }
            }

            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }
}
