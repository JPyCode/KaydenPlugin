package com.jpycode.kayden.economy.listeners;

import com.jpycode.kayden.economy.market.ItemVenda;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.jpycode.kayden.economy.database.BalanceAsync.getBalance;
import static com.jpycode.kayden.economy.database.BalanceAsync.setBalance;
import static com.jpycode.kayden.economy.database.MarketItem.getItensVendidos;
import static com.jpycode.kayden.economy.gui.MarketPriceGUI.openPriceGUI;
import static com.jpycode.kayden.economy.market.Market.*;

public class MarketListener implements Listener {
    @EventHandler
    public void onMarketBuy(InventoryClickEvent e) {
        if(!e.getView().getTitle().equalsIgnoreCase("itens a venda")) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem == null || !clickedItem.hasItemMeta()) return;
        System.out.println("maoi");
        ItemVenda itemVenda = getItemVendaByItem(clickedItem);
        if(itemVenda == null) return;
        System.out.println("passou...");

        double itemPrice = itemVenda.preco();
        UUID vendedorUUID = UUID.fromString(itemVenda.vendedor());

        getBalance(player).thenAccept(balance -> {
            if(balance < itemPrice){
                player.sendMessage(ChatColor.RED + "Você não tem dinheiro suficiente para comprar este item!");
                return;
            }

            setBalance(player, balance - itemPrice);
            System.out.println("teste");
            getBalance(Bukkit.getPlayer(vendedorUUID)).thenAccept(vendedorBalance -> {
                setBalance(Bukkit.getPlayer(vendedorUUID), vendedorBalance + itemPrice);
                System.out.println("teste dentro");
            });
        });

        player.getInventory().addItem(itemVenda.item());
        player.sendMessage(ChatColor.GREEN + "Você comprou " + ChatColor.GOLD + clickedItem.getItemMeta().getDisplayName() +
                ChatColor.GREEN + " por $" + ChatColor.GOLD + itemPrice);
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

    @EventHandler
    public void onSellItemClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Escolha um item")) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        List<String> lore = List.of();

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
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

    public ItemVenda getItemVendaByItem(ItemStack itemStack) {
        for(ItemVenda itemVenda : getItensVendidos()) {
            System.out.println("roletando");
            if (itemVenda.item().isSimilar(itemStack)) {
                System.out.println("Achei mestre");
                return itemVenda;
            }
        }
        return null;
    }
}
