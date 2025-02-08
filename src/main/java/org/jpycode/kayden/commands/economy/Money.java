package org.jpycode.kayden.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.jpycode.kayden.commands.economy.Eco.getBalance;

public class Money implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(String.format(ChatColor.GREEN + "Your balance: " + ChatColor.GOLD + "$ %.2f", getBalance(player.getUniqueId())));
        } else if(args.length == 1 && Bukkit.getPlayer(args[0]) instanceof Player target) {
            player.sendMessage(String.format(ChatColor.GREEN + "Your balance: " + ChatColor.GOLD + "$ %.2f", getBalance(target.getUniqueId())));
        } else {
            player.sendMessage(ChatColor.RED + "Invalid usage.");
        }
        return true;
    }
}
