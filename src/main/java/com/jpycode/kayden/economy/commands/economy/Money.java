package com.jpycode.kayden.economy.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.jpycode.kayden.economy.database.BalanceAsync.getBalance;


public class Money implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }

        if(args.length == 0) {
            getBalance(player).thenAccept(balance -> {
                player.sendMessage(String.format(ChatColor.GREEN + "Saldo: " + ChatColor.GOLD + "R$ %.2f", balance));
            });
        } else if(args.length == 1 && Bukkit.getPlayer(args[0]) instanceof Player target) {
            getBalance(target).thenAccept(balance -> {
                player.sendMessage(String.format(ChatColor.GREEN + "Saldo de " +
                        ChatColor.LIGHT_PURPLE + target.getName() +
                        ChatColor.GREEN + ": "+ ChatColor.GOLD + "R$ %.2f", balance));

            });
        } else {
            player.sendMessage(ChatColor.RED + "Uso inválido.");
        }
        return true;
    }
}
