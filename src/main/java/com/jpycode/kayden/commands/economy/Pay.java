package com.jpycode.kayden.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.jpycode.kayden.commands.economy.Eco.getBalance;
import static com.jpycode.kayden.commands.economy.Eco.setBalance;

public class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        double value = Double.parseDouble(args[1]);
        Player target = Bukkit.getPlayer(args[0]);

        if (getBalance(player.getUniqueId()) < value) {
            player.sendMessage(ChatColor.RED + "Insufficient balance");
            return true;
        }

        if(player.getUniqueId() == target.getUniqueId()) {
            player.sendMessage(ChatColor.RED + "You can't send money to yourself.");
            return true;
        }

        payExec(player, target, value);

        return false;
    }

    private void payExec(Player player, Player target, double value) {
        setBalance(player.getUniqueId(), getBalance(player.getUniqueId()) - value);
        setBalance(target.getUniqueId(), getBalance(target.getUniqueId()) + value);

        player.sendMessage(ChatColor.GREEN + "You sent $ " + value + " to " + target.getName());
        target.sendMessage(ChatColor.GOLD + "You received " + value + " from " + player.getName());
    }
}
