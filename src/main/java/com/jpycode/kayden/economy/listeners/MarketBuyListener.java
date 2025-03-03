package com.jpycode.kayden.economy.listeners;

import com.jpycode.kayden.economy.market.ItemVenda;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.jpycode.kayden.economy.database.BalanceAsync.getBalance;
import static com.jpycode.kayden.economy.database.BalanceAsync.setBalance;
import static com.jpycode.kayden.economy.database.MarketItem.getItensVendidos;

public class MarketBuyListener implements Listener {

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
