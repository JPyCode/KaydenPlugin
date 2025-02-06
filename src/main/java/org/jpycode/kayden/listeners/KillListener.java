package org.jpycode.kayden.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jpycode.kayden.scoreboard.MainScoreboard;

import java.util.HashMap;
import java.util.List;

public class KillListener implements Listener {

    private final HashMap<String, Integer> playerKills;

    public KillListener(HashMap<String, Integer> playerKills, MainScoreboard mainScoreboard) {
        this.playerKills = playerKills;
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
            int kills = playerKills.getOrDefault(playerName, 0) + 1;
            playerKills.put(playerName, kills);
            killer.sendMessage("🔥 Você matou uma entidade! Total de kills: " + kills);
        }

    }
}
