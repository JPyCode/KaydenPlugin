package com.jpycode.kayden.rpg.stats.gui;

import com.jpycode.kayden.rpg.stats.database.StatsManager;
import com.jpycode.kayden.rpg.stats.model.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class StatusGUI implements Listener {

    private final StatsManager statsManager;

    public StatusGUI(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    // Abre a GUI para o jogador
    public void open(Player player) {
        PlayerStats stats = statsManager.getStats(player.getUniqueId());
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Seus Status");

        gui.setItem(0, createStatusItem(Material.GOLDEN_APPLE, "Vida", stats.getHealth()));
        gui.setItem(1, createStatusItem(Material.REDSTONE, "Força", stats.getStrength()));
        gui.setItem(2, createStatusItem(Material.FEATHER, "Agilidade", stats.getAgility()));
        gui.setItem(3, createStatusItem(Material.SHIELD, "Defesa", stats.getDefense()));
        gui.setItem(4, createStatusItem(Material.ENDER_EYE, "Inteligência", stats.getIntelligence()));

        player.openInventory(gui);
    }

    // Cria um item de status com informações personalizadas
    private ItemStack createStatusItem(Material material, String name, int value) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + name);
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Valor atual: " + ChatColor.GREEN + value));
            item.setItemMeta(meta);
        }
        return item;
    }

    // Evento para manipular os cliques na GUI
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Seus Status")) {
            event.setCancelled(true); // Impede a movimentação de itens na GUI

            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();

            if (slot >= 0 && slot <= 4) { // Verifica se clicou em um slot válido
                UUID playerUUID = player.getUniqueId();
                PlayerStats status = statsManager.getStats(playerUUID);

                switch (slot) {
                    case 0 -> status.setHealth(status.getHealth() + 1);
                    case 1 -> status.setStrength(status.getStrength() + 1);
                    case 2 -> status.setAgility(status.getAgility() + 1);
                    case 3 -> status.setDefense(status.getDefense() + 1);
                    case 4 -> status.setIntelligence(status.getIntelligence() + 1);
                }

                statsManager.saveStats(status);
                player.sendMessage(ChatColor.GREEN + "Status atualizado!");
                open(player); // Reabre a GUI para atualizar os valores
            }
        }
    }
}
