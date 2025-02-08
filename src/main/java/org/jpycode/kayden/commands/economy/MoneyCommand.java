package org.jpycode.kayden.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jpycode.kayden.Kayden;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MoneyCommand implements CommandExecutor {
    private final File economyFile;
    private static FileConfiguration economyConfig = null;

    public MoneyCommand() {
        this.economyFile = new File(Kayden.getInstance().getDataFolder(), "economy.yml");
        economyConfig = YamlConfiguration.loadConfiguration(economyFile);
    }

    public static double getBalance(UUID playerID) {
        return economyConfig.getDouble(playerID.toString(), 0.0);
    }

    public void setBalance(UUID playerID, double value) {
        economyConfig.set(playerID.toString(), value);
        saveConfig();
    }

    private void saveConfig() {
        try {
            economyConfig.save(economyFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Error at saving file economy.yml");
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if(command.getName().equalsIgnoreCase("bal") || command.getName().equalsIgnoreCase("balance"))
            player.sendMessage(String.format(ChatColor.GREEN + "Your balance: " + ChatColor.GOLD + "$ %.2f", getBalance(player.getUniqueId())));

        switch (args.length) {
            case 0:
                player.sendMessage(String.format(ChatColor.GREEN + "Your balance: " + ChatColor.GOLD + "$ %.2f", getBalance(player.getUniqueId())));
                return true;

            case 3:
                Player target = Bukkit.getPlayer(args[1]);

                if((args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) && !player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to execute that command.");
                    return true;
                }

                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                double value;

                try {
                    value = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Type a valid value.");
                    return true;
                }

                switch (args[0].toLowerCase()) {
                    case "add":
                        systemAddBalance(player, target, value, true);
                        return true;


                    case "remove":
                        systemRemoveBalance(player, target, value, false);
                        return true;

                    case "pay":
                        if (getBalance(player.getUniqueId()) < value) {
                            player.sendMessage(ChatColor.RED + "Insufficient balance");
                            return true;
                        }

                        if(player.getUniqueId() == target.getUniqueId()) {
                            player.sendMessage(ChatColor.RED + "You can't send money to yourself.");
                            return true;
                        }

                        setBalance(player.getUniqueId(), getBalance(player.getUniqueId()) - value);
                        setBalance(target.getUniqueId(), getBalance(target.getUniqueId()) + value);

                        player.sendMessage(ChatColor.GREEN + "You sent $ " + value + " to " + target.getName());
                        target.sendMessage(ChatColor.GOLD + "You received " + value + " from " + player.getName());
                        return true;
                }

            default:
                if(player.isOp()) {
                    player.sendMessage("Use: /money [add/remove/pay] <nick> <quantity>");
                } else player.sendMessage("Use: /money pay <nick> <quantity>");

                return true;
        }
    }

    public void systemAddBalance(Player player, Player target, double value, boolean message) {
        setBalance(target.getUniqueId(), getBalance(target.getUniqueId()) + value);
        player.sendMessage(ChatColor.GREEN + "You added $ " + value + " to " + target.getName());
        if(message) target.sendMessage(ChatColor.GOLD + "You received $ " + value + " from system.");
    }

    public void systemRemoveBalance(Player player, Player target, double value, boolean message) {
        setBalance(target.getUniqueId(), getBalance(target.getUniqueId()) - value);
        player.sendMessage(ChatColor.RED + "You removed $ " + value + " from " + target.getName());
        if(message) target.sendMessage(ChatColor.RED + "$ " + value + " has been taken of your balance");
    }
}