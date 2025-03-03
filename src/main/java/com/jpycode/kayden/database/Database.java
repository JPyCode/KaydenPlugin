package com.jpycode.kayden.database;

import com.jpycode.kayden.Kayden;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

import static com.jpycode.kayden.scoreboard.MainScoreboard.updateMoneyInCache;

public class Database {
    @Getter
    private static final JavaPlugin plugin = Kayden.getInstance();
    @Getter
    public static Connection connection;

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
                plugin.getLogger().info("Conexão com MySQL estabelecida <" + database + ">!");
            } catch (SQLException | ClassNotFoundException e) {
                plugin.getLogger().severe("Erro ao conectar ao banco de dados: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Conexão com MySQL encerrada.");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao fechar conexão com MySQL: " + e.getMessage());
        }
    }

    public static CompletableFuture<Integer> getKills(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT kills FROM player_kills WHERE uuid = ?";
            try (PreparedStatement stmt = getConnection().prepareStatement(query)) {

                stmt.setString(1, player.getUniqueId().toString());
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    return rs.getInt("kills");
                } else {
                    System.out.println("Nenhum dado encontrado para " + player.getName());
                    return 0; // Retorna 0 se o jogador ainda não tiver registro
                }

            } catch (SQLException e) {
                plugin.getLogger().severe("Erro ao obter kills de " + player.getName() + ": " + e.getMessage());
                return 0;
            }
        });
    }
}
