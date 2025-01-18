package org.jpycode.kayden.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            if(p.getWorld().getName().equals("minigames"))
            if(p.getScoreboard() != null && p.getScoreboard().getObjective("Example") != null)
                updateBoard(p);
            else createBoard(p);



        }

    }

    private void createBoard(Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("Example", Criteria.DUMMY, Component.text("§6Example Scoreboard"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(NamedTextColor.WHITE + " Oi");

        p.setScoreboard(board);
    }

    private void updateBoard(Player p) {

        Scoreboard board = p.getScoreboard();

        Team team1 = board.getTeam("team1");
        assert team1 != null;
        team1.suffix(Component.text(NamedTextColor.WHITE + "" + (p.getStatistic(Statistic.WALK_ONE_CM) + p.getStatistic(Statistic.SPRINT_ONE_CM)) + " cm"));

    }

    public static Board getInstance() {
        return instance;
    }
}
