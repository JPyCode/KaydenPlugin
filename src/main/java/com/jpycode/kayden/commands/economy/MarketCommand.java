package com.jpycode.kayden.commands.economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import com.jpycode.kayden.database.Database;
import com.jpycode.kayden.gui.marketGUI.MarketGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MarketCommand implements CommandExecutor {
    private final MarketGUI marketGUI;

    public MarketCommand(MarketGUI marketGUI) {
        this.marketGUI = marketGUI;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (sender instanceof Player player) {

            switch (args.length) {
                case 0 -> {
                    marketGUI.openGUI(player);
                    return true;
                }
                case 2 -> {
                    double price;

                    try {
                        price = Double.parseDouble(args[1]);
                    } catch (NumberFormatException ex) {
                        player.sendMessage(ChatColor.RED + "Type a valid value.");
                        return true;
                    }

                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() == Material.AIR) {
                        player.sendMessage(ChatColor.RED + "You need to hold an item to sell it.");
                        return true;
                    }

                    String itemData = item.serialize().toString();
                    String query = "INSERT INTO market_items (seller_uuid, seller_name, item_data, price) VALUES (?, ?, ?, ?)";
                    try(Connection conn = Database.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, player.getUniqueId().toString());
                        stmt.setString(2, player.getName());
                        stmt.setString(3, itemData);
                        stmt.setDouble(4, price);
                        stmt.executeUpdate();
                    } catch (SQLException exception) {
                        player.sendMessage(ChatColor.RED + "Error listing the item on the market.");
                        exception.printStackTrace();
                        return true;
                    }
                    player.getInventory().setItemInMainHand(null);
                    player.sendMessage(ChatColor.GREEN + "You put an item on sale for $" + price);
                    return true;
                }
                default -> {
                    player.sendMessage("Use: /market sell <value>");
                    return true;
                }
            }
        }
        sender.sendMessage("Only players can use this command.");

        return true;
    }
}
