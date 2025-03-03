package com.jpycode.kayden.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import com.jpycode.kayden.Kayden;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.jpycode.kayden.database.Database.*;

public class MainScoreboard {

    private final Kayden plugin;
    private static final Map<Player, Integer> playerKillsCache = new HashMap<>();
    private static final Map<Player, Double> playerMoneyCache = new HashMap<>();

    public MainScoreboard(Kayden plugin) {
        this.plugin = plugin;

        // Atualiza o scoreboard a cada segundo
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard == null) {
            scoreboard = manager.getNewScoreboard();
        }

        // Cria um objetivo sidebar se não existir
        Objective objective = scoreboard.getObjective("stats");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("stats", "dummy", ChatColor.RED + "📊 Estatísticas");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        // Atualiza os valores do jogador
        updatePlayerStats(player, scoreboard, objective);
    }

    private void updatePlayerStats(Player player, Scoreboard scoreboard, Objective objective) {
        Integer kills = playerKillsCache.get(player);
        Double money = playerMoneyCache.get(player);

        if (kills == null) {
            getKills(player).thenAccept(killsFromDB -> {
                playerKillsCache.put(player, killsFromDB);
                updateScoreboardWithStats(player, scoreboard, objective, killsFromDB, money);
            });
        } else {
            updateScoreboardWithStats(player, scoreboard, objective, kills, money);
        }
    }

    private void updateScoreboardWithStats(Player player, Scoreboard scoreboard, Objective objective, Integer kills, Double money) {
        if (money == null) {
            getBalance(player).thenAccept(moneyFromDB -> {
                playerMoneyCache.put(player, moneyFromDB);
                setScores(player, scoreboard, objective, kills, moneyFromDB);
            });
        } else {
            setScores(player, scoreboard, objective, kills, money);
        }
    }

    private void setScores(Player player, Scoreboard scoreboard, Objective objective, Integer kills, Double money) {
        // Criando equipes para atualizar os valores sem criar novas linhas
        createOrUpdateTeam(scoreboard, "playerName", ChatColor.GOLD + "👤 ", player.getName(), 3);
        createOrUpdateTeam(scoreboard, "kills", ChatColor.DARK_PURPLE + "👊 Kills: ", String.valueOf(kills), 2);
        createOrUpdateTeam(scoreboard, "money", ChatColor.GREEN + "💰 Dinheiro: ", String.valueOf(money), 1);

        player.setScoreboard(scoreboard);
    }

    private void createOrUpdateTeam(Scoreboard scoreboard, String teamName, String prefix, String value, int score) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.addEntry(ChatColor.WHITE + prefix); // Entrada única para cada linha
        }

        // Atualiza os valores sem criar novas linhas
//        team.setPrefix(ChatColor.DARK_PURPLE + "");
        team.setSuffix(ChatColor.YELLOW + value);

        Objective objective = scoreboard.getObjective("stats");
        if (objective != null) {
            objective.getScore(ChatColor.stripColor(prefix)).setScore(score);
        }
    }

    public static void updateKillsInCache(Player player, int kills) {
        playerKillsCache.put(player, kills);
    }

    public static void updateMoneyInCache(Player player, double money) {
        playerMoneyCache.put(player, money);
    }
}
