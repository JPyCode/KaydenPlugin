package com.jpycode.kayden.database;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class Database {
    private static JavaPlugin plugin;
    private static Connection connection;

    public Database(JavaPlugin plugin) {
        Database.plugin = plugin;
    }

    public static CompletableFuture<Void> connect() {
        return CompletableFuture.runAsync(() -> {
            try {
                if (connection != null && !connection.isClosed()) return;

                Class.forName("com.mysql.cj.jdbc.Driver");

                String host = plugin.getConfig().getString("database.host");
                int port = plugin.getConfig().getInt("database.port");
                String database = plugin.getConfig().getString("database.database");
                String username = plugin.getConfig().getString("database.username");
                String password = plugin.getConfig().getString("database.password");

                String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
                connection = DriverManager.getConnection(url, username, password);
                plugin.getLogger().info("Conexão com MySQL estabelecida!");
            } catch (SQLException | ClassNotFoundException e) {
                plugin.getLogger().severe("Erro ao conectar ao banco de dados: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    public static void close() {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[Kayden] Connection with MySql closed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try{
            if(connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void savePlayerData(Player player) {
        String query = "INSERT INTO players_table (uuid, name, kills) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE name = ?";

        try(PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());
            stmt.setInt(3, 0);
            stmt.setString(4, player.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO economy (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = ?")) {
                    stmt.setString(1, player.getUniqueId().toString());
                    stmt.setDouble(2, value);
                    stmt.setDouble(3, value);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Erro ao definir saldo de " + player.getName() + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
