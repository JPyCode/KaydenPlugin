package com.jpycode.kayden.economy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.jpycode.kayden.economy.gui.ShopGUI.createShopGUI;

public class SwordShop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Apenas jogadores podem executar este comando!");
            return true;
        }

        Inventory shopInventory = createShopGUI();
        if(shopInventory == null) {
            player.sendMessage(ChatColor.RED + "A loja está vazia ou não foi configurada corretamente.");
            return true;
        }
        player.openInventory(shopInventory);
        return true;
    }
}
