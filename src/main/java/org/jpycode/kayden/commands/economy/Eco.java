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

public class Eco implements CommandExecutor {
    private static final File economyFile = new File(Kayden.getInstance().getDataFolder(), "economy.yml");
    private static final FileConfiguration economyConfig = YamlConfiguration.loadConfiguration(economyFile);



    public static double getBalance(UUID playerID) {
        return economyConfig.getDouble(playerID.toString(), 0.0);
    }

    public static void setBalance(UUID playerID, double value) {
        economyConfig.set(playerID.toString(), value);
        saveConfig();
    }

    private static void saveConfig() {
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

        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);

            if ((args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) && !player.isOp()) {
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
                case "give":
                    systemAddBalance(player, target, value, true);
                    return true;

                case "remove":
                    systemRemoveBalance(player, target, value, false);
                    return true;

                case "set":
                    setBalance(target.getUniqueId(), value);

                default:
                    player.sendMessage(ChatColor.RED + "Invalid usage.");
                    return true;
            }
        }
        if (player.isOp()) {
            player.sendMessage("Use: /money [give/remove/pay] <nick> <quantity>");
        } else player.sendMessage("Use: /money pay <nick> <quantity>");

        return true;
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