package com.jpycode.kayden.rpg.status.gui;

import com.jpycode.kayden.Kayden;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatusGUI implements Listener {
    private final JavaPlugin plugin = Kayden.getInstance();
    private final Map<UUID, PlayerStatus> playerStatusMap = new HashMap<>();
    public void openStatusGUI(Player player) {

        UUID playerUUID = player.getUniqueId();
        PlayerStatus status = playerStatusMap.computeIfAbsent(playerUUID, k -> new PlayerStatus());

        Inventory gui = plugin.getServer().createInventory(null, 45, ChatColor.GOLD + "Stats Screen");

        // Strength
        ItemStack strengthItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta strengthMeta = strengthItem.getItemMeta();
        strengthMeta.setDisplayName(ChatColor.RED + "Strength: " + status.getStrength());
        strengthItem.setItemMeta(strengthMeta);
        gui.setItem(4, strengthItem);

        // Defense
        ItemStack defenseItem = new ItemStack(Material.SHIELD);
        ItemMeta defenseMeta = defenseItem.getItemMeta();
        defenseMeta.setDisplayName(ChatColor.BLUE + "Defense: " + status.getDefense());
        defenseItem.setItemMeta(defenseMeta);
        gui.setItem(0, defenseItem);

        // Open GUI
        player.openInventory(gui);
    }

    @EventHandler
    public void onPlayerClickGUI(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        UUID playerUUID = p.getUniqueId();
        PlayerStatus status = playerStatusMap.computeIfAbsent(playerUUID, k -> new PlayerStatus());
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "stats screen")) {

            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;

            switch (e.getSlot()) {
                case 0:
                    status.setDefense(status.getDefense() + 1);
                    p.sendMessage("Você clicou no botão de defesa");
                    System.out.println(p.getName() + " clicou no botão de defesa.");
                    openStatusGUI(p);
                    break;
                case 4:
                    status.setStrength(status.getStrength() + 1);
                    p.sendMessage("Você clicou no botão de força");
                    System.out.println(p.getName() + " clicou no botão de força.");
                    openStatusGUI(p);
                    break;
            }
        }
    }

    @Getter
    @Setter
    private static class PlayerStatus {
        private int strength;
        private int defense;
    }
}
