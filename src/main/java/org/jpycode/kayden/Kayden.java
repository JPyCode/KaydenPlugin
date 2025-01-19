package org.jpycode.kayden;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jpycode.kayden.commands.OpenSwordsGUICommand;
import org.jpycode.kayden.gui.SwordsGUI;
import org.jpycode.kayden.listeners.ThunderSword;
import org.jpycode.kayden.scoreboard.Board;

public final class Kayden extends JavaPlugin {
    private BukkitTask task;

    @Override
    public void onEnable() {
        getLogger().info("@ Kayden Plugin enabled");
        /* Scoreboard */
        task = getServer().getScheduler().runTaskTimer(this, Board.getInstance(), 0, 20);

        /* Listeners */
        getServer().getPluginManager().registerEvents(new SwordsGUI(this), this);
        getServer().getPluginManager().registerEvents(new ThunderSword(), this);

        /* Commands */
        getCommand("swords").setExecutor(new OpenSwordsGUICommand(new SwordsGUI(this)));

    }

    @Override
    public void onDisable() {
        getLogger().info("@ Kayden Plugin disabled");
    }

}