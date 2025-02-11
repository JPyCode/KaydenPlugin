package com.jpycode.kayden.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.jpycode.kayden.database.Database.getBalance;
import static com.jpycode.kayden.database.Database.setBalance;


public class Eco implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "Você não possui permissão para executar este comando.");
            return true;
        }

        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Jogador não encontrado.");
                return true;
            }
            double value;
            try {
                value = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Digite um valor válido.");
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "add":
                    systemAddBalance(player, target, value, true);
                    return true;

                case "remove":
                    systemRemoveBalance(player, target, value, false);
                    return true;

                case "set":
                    setBalance(target, value);
                    player.sendMessage("Saldo do jogador " + target.getName() + " foi definido como " + value);
                    return true;

                default:
                    player.sendMessage(ChatColor.RED + "Uso inválido.");
                    return true;
            }
        }

        return true;
    }

    public static void systemAddBalance(Player player, Player target, double value, boolean message) {
        getBalance(target).thenAccept(balance -> {
            double newBalance = balance + value;
            setBalance(target, newBalance);
            player.sendMessage(ChatColor.GREEN + "Você adicionou R$ " + value + " na conta do jogador " + ChatColor.LIGHT_PURPLE + target.getName());
            if (message) target.sendMessage(ChatColor.GOLD + "Você recebeu R$ " + value + ".");
        });

    }

    public void systemRemoveBalance(Player player, Player target, double value, boolean message) {
        getBalance(target).thenAccept(balance -> {
            if (balance >= value) {
                double newBalance = balance - value;
                setBalance(target, newBalance);
                player.sendMessage(ChatColor.RED + "Você removeu R$ " + value + " do jogador " + target.getName());
                if (message) target.sendMessage(ChatColor.RED + "R$ " + value + " retirados da sua conta.");
            } else player.sendMessage(ChatColor.BLACK + "<"+
                    ChatColor.LIGHT_PURPLE + target.getName() +
                    ChatColor.BLACK + ">" + ChatColor.RED +
                    " não possui esta quantia.");
        });
    }
}