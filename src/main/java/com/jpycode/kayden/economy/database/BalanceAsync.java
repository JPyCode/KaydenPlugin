package com.jpycode.kayden.economy.database;

import com.jpycode.kayden.database.Database;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import static com.jpycode.kayden.database.Database.connect;

public class BalanceAsync {
    private static final Connection connection = Database.getConnection();
    private static final JavaPlugin plugin = Database.getPlugin();

    public static CompletableFuture<Double> getBalance(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (connection == null || connection.isClosed()) {
                    connect().join();
                }
                try (PreparedStatement stmt = connection.prepareStatement("SELECT balance FROM economy WHERE uuid = ?")) {
                    stmt.setString(1, player.getUniqueId().toString());
                    ResultSet rs = stmt.executeQuery();
                    return rs.next() ? rs.getDouble("balance") : 0.0;
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Erro ao obter saldo de " + player.getName() + ": " + ex.getMessage());
                throw new RuntimeException("Erro ao obter saldo", ex);
            }
        });
    }

    public static void setBalance(Player player, double value) {
        CompletableFuture.runAsync(() -> {
            try {
                if (connection == null || connection.isClosed()) {
                    connect().join();
                }
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO economy (uuid, name, balance) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?")) {
                    stmt.setString(1, player.getUniqueId().toString());
                    stmt.setString(2, player.getName());
                    stmt.setDouble(3, value);
                    stmt.setDouble(4, value);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Erro ao definir saldo de " + player.getName() + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
