package com.jpycode.kayden.commands.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.jpycode.kayden.Kayden;
import com.jpycode.kayden.database.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Eco implements CommandExecutor {
    private static final File economyFile = new File(Kayden.getInstance().getDataFolder(), "economy.yml");
    private static final FileConfiguration economyConfig = YamlConfiguration.loadConfiguration(economyFile);


    public static double getBalance(UUID playerID) {
//        return economyConfig.getDouble(playerID.toString(), 0.0);
        String query = "SELECT balance FROM economy WHERE uuid = ?";
        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerID.toString());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    public static void setBalance(UUID playerID, double value) {
//        economyConfig.set(playerID.toString(), value);
//        saveConfig();
        String query = "INSERT INTO economy (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = ?";
        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerID.toString());
            stmt.setDouble(2, value);
            stmt.setDouble(3, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You do not have permission to execute that command.");
            return true;
        }

        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
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

                case "set":
                    setBalance(target.getUniqueId(), value);
                    return true;

                default:
                    player.sendMessage(ChatColor.RED + "Invalid usage.");
                    return true;
            }
        }

        return true;
    }

    public void systemAddBalance(Player player, Player target, double value, boolean message) {
        setBalance(target.getUniqueId(), getBalance(target.getUniqueId()) + value);
        player.sendMessage(ChatColor.GREEN + "You added $ " + value + " to " + target.getName());
        if (message) target.sendMessage(ChatColor.GOLD + "You received $ " + value + " from system.");
    }

    public void systemRemoveBalance(Player player, Player target, double value, boolean message) {
        if (getBalance(target.getUniqueId()) >= value) {
            setBalance(target.getUniqueId(), getBalance(target.getUniqueId()) - value);
            player.sendMessage(ChatColor.RED + "You removed $ " + value + " from " + target.getName());
            if (message) target.sendMessage(ChatColor.RED + "$ " + value + " has been taken of your balance");
        } else player.sendMessage(ChatColor.BLACK + "<"+
                ChatColor.LIGHT_PURPLE+target.getName()+
                ChatColor.BLACK+">" + ChatColor.RED +
                " does not have that amount");
    }
}