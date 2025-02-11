package com.jpycode.kayden.commands.economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import com.jpycode.kayden.gui.marketGUI.MarketMenu;

import java.util.ArrayList;
import java.util.List;


public class MarketCommand implements CommandExecutor {
    private final List<ItemStack> itemsForSale = new ArrayList<>();
    private final MarketMenu marketMenu;

    public MarketCommand(MarketMenu marketMenu) {
        this.marketMenu = marketMenu;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (sender instanceof Player player) {
            if(args.length == 0) marketMenu.openGUI(player);
            else player.sendMessage(ChatColor.RED + "Invalid usage.");
            return true;
        }
        sender.sendMessage("Only players can use this command.");

        return true;
    }
}
