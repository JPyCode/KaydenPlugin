package com.jpycode.kayden.economy.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.jpycode.kayden.economy.database.BalanceAsync.getBalance;
import static com.jpycode.kayden.economy.database.BalanceAsync.setBalance;

public class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }
        double value = Double.parseDouble(args[1]);
        Player target = Bukkit.getPlayer(args[0]);
        if (player.getUniqueId() == target.getUniqueId()) {
            player.sendMessage(ChatColor.RED + "Você não pode enviar dinheiro para si mesmo!");
            return true;
        }
        checkBalanceAndPay(player, target, value);

        return false;
    }

    public void checkBalanceAndPay(Player player, Player target, double value) {
        getBalance(player).thenAccept(balance -> {
            if (balance < value) {
                player.sendMessage(ChatColor.RED + "Saldo insuficiente.");
                return;
            }
            payExec(player, target, value);
        });
    }

    private void payExec(Player player, Player target, double value) {
        getBalance(player).thenCompose(playerBalance ->
                getBalance(target).thenAccept(targetBalance -> {

                    setBalance(player, playerBalance - value);
                    setBalance(target, targetBalance - value);

                    player.sendMessage(ChatColor.GREEN + "Você enviou R$ " + value + " para " + target.getName());
                    target.sendMessage(ChatColor.GOLD + "Você recebeu R$ " + value + " de " + player.getName());


                }));

    }
}
