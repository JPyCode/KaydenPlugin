package com.jpycode.kayden.rpg.stats.command;

import com.jpycode.kayden.rpg.stats.database.StatsManager;
import com.jpycode.kayden.rpg.stats.gui.StatusGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final StatsManager statsManager;

    public StatsCommand(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }
        StatusGUI gui = new StatusGUI(statsManager);
        gui.open(player);

        return true;
    }
}
