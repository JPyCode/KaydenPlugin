package com.jpycode.kayden.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import com.jpycode.kayden.Kayden;
import com.jpycode.kayden.listeners.KillListener;

import java.util.HashMap;

public class MainScoreboard {

    private final Kayden plugin; // Substitua pelo nome da sua classe principal

    public MainScoreboard(Kayden plugin) {
        this.plugin = plugin;
        startScoreboardUpdater();
    }

    private void startScoreboardUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Atualiza a cada 20 ticks (1 segundo)
    }

    public void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("kills", "dummy", ChatColor.RED + "📊 Estatísticas");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Obtém o número de kills ou define como 0

        int kills = KillListener.getKills(player);
        Score killScore = objective.getScore(ChatColor.DARK_PURPLE + "👊 Kills: " + ChatColor.YELLOW + kills);
        // Define a posição no Scoreboard, neste caso, segunda linha
        killScore.setScore(1);

        player.setScoreboard(scoreboard);
    }
}
