package com.jpycode.kayden.economy.market;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.jpycode.kayden.economy.database.MarketItem.getItensVendidos;
import static com.jpycode.kayden.economy.gui.MarketPriceGUI.openPriceGUI;

public class Market implements Listener, CommandExecutor {

    private static final Map<Player, ItemStack> sellingItems = new HashMap<>();
    public static Map<ItemStack, Integer> mapPrecos = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Apenas jogadores podem executar este comando.");
            return true;
        }
        openMarketGUI(player);
        return true;
    }

    public static void openMarketGUI(Player player) {
        Inventory marketGui = Bukkit.createInventory(null, 27, "Mercado");

        ItemStack sellItem = new ItemStack(Material.EMERALD);
        ItemMeta sellMeta = sellItem.getItemMeta();
        sellMeta.setDisplayName("Vender item");
        sellItem.setItemMeta(sellMeta);

        ItemStack showItems = new ItemStack(Material.DIAMOND);
        ItemMeta showMeta = sellItem.getItemMeta();
        showMeta.setDisplayName("Ver itens a venda");
        showItems.setItemMeta(showMeta);

        marketGui.setItem(12, sellItem);
        marketGui.setItem(14, showItems);
        player.openInventory(marketGui);
    }

    @EventHandler
    public void onMarketClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("Mercado")) {
            event.setCancelled(true);
            if (event.getSlot() == 12) {
                openSellGUI(player);
            } else if (event.getSlot() == 14) {
                openSellingItemsGUI(player);
            }
        }
    }

    public static void openSellingItemsGUI(Player player) {
        Inventory marketGUI = Bukkit.createInventory(null, 54, "Itens a venda");
        marketGUI.clear();
        for (ItemVenda item : getItensVendidos()) {
            ItemStack itemStack = item.item();
            if (itemStack == null) {
                continue;
            }
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>(item.lore());
            if (lore.isEmpty() || (lore.size() == 1 && lore.get(0).trim().equals("[]"))) lore.clear();
            lore.add("");
            lore.add(ChatColor.GREEN + "💰 Preço: $" + item.preco());
            lore.add(ChatColor.GRAY + "👤 Vendedor: " + Bukkit.getPlayer(UUID.fromString(item.vendedor())).getName());

            meta.setLore(lore);
            mapPrecos.put(itemStack, item.preco());
            lore.clear();
            itemStack.setItemMeta(meta);

            marketGUI.addItem(itemStack);
        }

        player.openInventory(marketGUI);
    }


    public static void openSellGUI(Player player) {
        Inventory sellGui = Bukkit.createInventory(null, InventoryType.CHEST, "Escolha um item");
        player.openInventory(sellGui);
    }

    @EventHandler
    public void onSellItemClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Escolha um item")) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        List<String> lore = List.of();

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            sellingItems.put(player, event.getCurrentItem());
            if (event.getCurrentItem().getItemMeta().getLore() == null) {
                System.out.println("É nulo pae");
            } else {
                lore = event.getCurrentItem().getItemMeta().getLore();
            }
            ItemStack eventItem = event.getCurrentItem();
            player.getInventory().setItem(event.getSlot(), null);
            player.closeInventory();
            player.sendMessage("Você selecionou " + eventItem.getType() + " para vender.");
            openPriceGUI(player, eventItem, lore);

        }
    }
}
