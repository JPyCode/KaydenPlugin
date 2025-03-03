package com.jpycode.kayden.listeners;

import com.jpycode.kayden.Kayden;
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.jpycode.kayden.database.Database.getKills;
import static com.jpycode.kayden.scoreboard.MainScoreboard.updateKillsInCache;

public class KillListener implements Listener {
    private final Plugin plugin = Kayden.getInstance();
    private final MainScoreboard scoreboard = new MainScoreboard(Kayden.getInstance());


    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer;
        if (e.getEntity().getKiller() instanceof Player p) {
            killer = p;
        } else {
            List<MetadataValue> metadata = e.getEntity().getMetadata("lastAttacker");
            if (!metadata.isEmpty()) {
                String killerName = metadata.get(0).asString();
                killer = e.getEntity().getServer().getPlayer(killerName);
            } else {
                killer = null;
            }
        }

        if (killer != null) {
            String playerName = killer.getName();

            // Incrementa as kills do jogador
            getKills(killer).thenAccept(kills -> {
                int killsUpdate = kills + 1;
                String query = "INSERT INTO player_kills (uuid, name, kills) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE kills = VALUES(kills)";
                try (PreparedStatement stmt = Database.getConnection().prepareStatement(query)) {
                    stmt.setString(1, killer.getUniqueId().toString());
                    stmt.setString(2, playerName);
                    stmt.setInt(3, killsUpdate);
                    stmt.executeUpdate();
                    System.out.println("Kills atualizadas para " + killer.getName() + ": " + killsUpdate);
                } catch (SQLException exception) {
                    System.err.println("Erro ao atualizar kills de " + killer.getName() + ": " + exception.getMessage());
                    throw new RuntimeException(exception);
                }
                killer.sendMessage("🔥 Você matou uma entidade! Total de kills: " + killsUpdate);
                updateKillsInCache(killer, killsUpdate);
            });
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p) {
            e.getEntity().setMetadata("lastAttacker", new FixedMetadataValue(plugin, p.getName()));
        }
    }
}
