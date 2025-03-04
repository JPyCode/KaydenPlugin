package com.jpycode.kayden.rpg.stats.database;

import com.jpycode.kayden.database.Database;
import com.jpycode.kayden.rpg.stats.model.PlayerStats;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class StatsManager {
    private final Connection connection;
    @Getter
    private static StatsManager instance;
    private final Map<UUID, PlayerStats> statsCache = new HashMap<>();

    public StatsManager() {
        this.connection = Database.getConnection();
        instance = this;
    }

    public PlayerStats getStats(UUID uuid) {
        if(statsCache.containsKey(uuid) ) {
            return statsCache.get(uuid);
        }

        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM player_stats WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                PlayerStats stats = new PlayerStats(
                        uuid,
                        rs.getInt("health"),
                        rs.getInt("strength"),
                        rs.getInt("agility"),
                        rs.getInt("defense"),
                        rs.getInt("intelligence")
                );
                statsCache.put(uuid, stats);
                return stats;
            } else {
                PlayerStats defaultStats = new PlayerStats(uuid, 20, 0, 0, 0, 0);
                saveStats(defaultStats);
                return defaultStats;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveStats(PlayerStats stats) {
        statsCache.put(stats.getPlayerUUID(), stats);

        try(PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO player_stats (uuid, health, strength, agility, defense, intelligence) VALUES (?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE health = ?, strength = ?, agility = ?, defense = ?, intelligence = ?")) {
            statement.setString(1, stats.getPlayerUUID().toString());
            statement.setInt(2, stats.getHealth());
            statement.setInt(3, stats.getStrength());
            statement.setInt(4, stats.getAgility());
            statement.setInt(5, stats.getDefense());
            statement.setInt(6, stats.getIntelligence());

            statement.setInt(7, stats.getHealth());
            statement.setInt(8, stats.getStrength());
            statement.setInt(9, stats.getAgility());
            statement.setInt(10, stats.getDefense());
            statement.setInt(11, stats.getIntelligence());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void removeStats(UUID uuid) {
        statsCache.remove(uuid);
        try(PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player_stats WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
