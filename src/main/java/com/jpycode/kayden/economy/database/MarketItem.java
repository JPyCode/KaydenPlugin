package com.jpycode.kayden.economy.database;

import com.jpycode.kayden.database.Database;
import com.jpycode.kayden.economy.market.ItemVenda;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jpycode.kayden.economy.gui.MarketPriceGUI.deserializeItemStack;
import static com.jpycode.kayden.economy.gui.MarketPriceGUI.serializeItemStack;

public class MarketItem {
    private static Connection connection = Database.getConnection();
    public static void saveMarketItem(Player player, ItemStack item, int price, List<String> lore) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO market (player_uuid, item_data, price, lore, expires_at) VALUES (?, ?, ?, ?, ?)"
        )) {

            String itemData = serializeItemStack(item); // Converte o ItemStack para String
            String playerUUID = player.getUniqueId().toString();

            // Define a expiração para 3 dias a partir de agora
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(3);
            Timestamp expirationTimestamp = Timestamp.valueOf(expiresAt);
            String loreToSave = String.join(" | ", lore);

            stmt.setString(1, playerUUID);
            stmt.setString(2, itemData);
            stmt.setInt(3, price);
            stmt.setString(4, loreToSave);
            stmt.setTimestamp(5, expirationTimestamp);

            stmt.executeUpdate();
            player.sendMessage(ChatColor.GREEN + "Item listado por 3 dias!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<ItemVenda> getItensVendidos() {
        List<ItemVenda> itens = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM market");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String vendedor = rs.getString("player_uuid");
                int preco = rs.getInt("price");
                String itemData = rs.getString("item_data");
                String loreString = rs.getString("lore");
                List<String> lore = loreString != null && !loreString.isEmpty() ?
                        Arrays.asList(loreString.split(" \\| "))
                        :
                        new ArrayList<>();


                ItemStack item = deserializeItemStack(itemData); // Desserializa o item
                itens.add(new ItemVenda(vendedor, preco, item, lore));
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return itens;
    }
}
