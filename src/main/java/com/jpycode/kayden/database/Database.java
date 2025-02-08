package com.jpycode.kayden.database;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    public static void connect() {
        String host = "jdbc:mysql://localhost:3306/players";
        String user = "root";
        String password = "0304";

        try {
            connection = DriverManager.getConnection(host, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}
