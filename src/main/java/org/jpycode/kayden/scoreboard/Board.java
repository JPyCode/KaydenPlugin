package org.jpycode.kayden.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.inject.Named;
import java.util.Objects;

public class Board implements Runnable {
    private final static Board instance = new Board();
    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getScoreboard().getObjective("Scoreboard") != null)
                updateBoard(p);
            else createBoard(p);
        }

    }

    private void createBoard(Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("Scoreboard", Criteria.DUMMY, Component.text("§4Server Scoreboard"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        p.setStatistic(Statistic.WALK_ONE_CM, 0);
        p.setStatistic(Statistic.SPRINT_ONE_CM, 0);

        Team team1 = board.registerNewTeam("team1");
        team1.addEntry("");
        team1.prefix(Component.text("§5Walked: "));
        team1.suffix(Component.text("0 m"));
        objective.getScore("").setScore(0);


        p.setScoreboard(board);
    }

    private void updateBoard(Player p) {

        Scoreboard board = p.getScoreboard();
        Team team1 = board.getTeam("team1");

        if(team1 != null)
            team1.suffix(Component.text( (p.getStatistic(Statistic.WALK_ONE_CM) + p.getStatistic(Statistic.SPRINT_ONE_CM)) / 100 + " m"));
    }

    public static Board getInstance() {
        return instance;
    }
}
