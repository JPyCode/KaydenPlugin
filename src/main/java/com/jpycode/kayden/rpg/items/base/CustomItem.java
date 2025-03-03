package com.jpycode.kayden.rpg.items.base;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class CustomItem implements Listener {
    private final String name;
    private final ItemStack itemStack;

    public CustomItem(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public abstract void onUse(Player player, Event event);

    protected static void setItemAppearance(ItemStack itemStack, String name, List<Component> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(lore);
            itemStack.setItemMeta(meta);
        }
    }

    public static ItemStack createBaseItem(Material material, String name, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        setItemAppearance(item, name, lore);
        return item;
    }


    /*
    Check if item is custom and cancel drop event.
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ItemStack droppedItem = e.getItemDrop().getItemStack();

        if (isCustomItem(droppedItem)) e.setCancelled(true);
    };

    private boolean isCustomItem(ItemStack item) {
        if(item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
            String displayName = String.valueOf(item.getItemMeta().displayName());
            return displayName.equalsIgnoreCase(this.getName());
        }
        return false;
    }


}
