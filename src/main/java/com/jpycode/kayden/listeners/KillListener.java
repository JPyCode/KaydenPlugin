package com.jpycode.kayden.listeners;

import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import com.jpycode.kayden.database.Database;
import com.jpycode.kayden.scoreboard.MainScoreboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class KillListener implements Listener {
    private final Plugin plugin;


    private static int kills = 0;

    public KillListener(MainScoreboard mainScoreboard, Plugin plugin) {
        this.plugin = plugin;
    }

    public static int getKills(Player player) {
        String getKills = "SELECT kills FROM players_table WHERE uuid = ?";
        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(getKills)) {
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                kills = rs.getInt("kills");
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return kills++;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = null;
        if (e.getEntity().getKiller() instanceof Player p) {
            killer = p;
        } else {
            List<MetadataValue> metadata = e.getEntity().getMetadata("lastAttacker");
            if (!metadata.isEmpty()) {
                String killerName = metadata.get(0).asString();
                killer = e.getEntity().getServer().getPlayer(killerName);
            }
        }

        if(killer != null) {
            String playerName = killer.getName();
            // Incrementa as kills do jogador
            getKills(killer);
            String query = "INSERT INTO players_table (uuid, name, kills) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE kills = ?";
            try(Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, killer.getUniqueId().toString());
                stmt.setString(2, playerName);
                stmt.setInt(3, kills);
                stmt.setInt(4, kills);
                stmt.executeUpdate();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
            killer.sendMessage("🔥 Você matou uma entidade! Total de kills: " + kills);
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player p) {
            e.getEntity().setMetadata("lastAttacker", new FixedMetadataValue(plugin, p.getName()));
        }
    }
}
