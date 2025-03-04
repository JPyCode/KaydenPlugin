package com.jpycode.kayden.rpg.stats.Listener;

import com.jpycode.kayden.rpg.stats.model.PlayerStats;
import com.jpycode.kayden.rpg.stats.database.StatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StatsListener implements Listener {
    private final StatsManager statsManager = StatsManager.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerStats stats = statsManager.getStats(e.getPlayer().getUniqueId());

        e.getPlayer().setMaxHealth(Math.max(stats.getHealth(), 1));
        e.getPlayer().setHealth(stats.getHealth());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayerStats stats = statsManager.getStats(e.getPlayer().getUniqueId());
        statsManager.saveStats(stats);
    }
}
