package org.jpycode.kayden;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jpycode.kayden.commands.OpenStatusGUICommand;
import org.jpycode.kayden.commands.OpenSwordsGUICommand;
import org.jpycode.kayden.gui.statusGUI.StatusGUI;
import org.jpycode.kayden.gui.swordsGUI.SwordsGUI;
import org.jpycode.kayden.listeners.DoubleJumpListener;
import org.jpycode.kayden.listeners.MentionChatListener;
import org.jpycode.kayden.listeners.ThunderSwordListener;
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
        getServer().getPluginManager().registerEvents(new ThunderSwordListener(), this);
        getServer().getPluginManager().registerEvents(new DoubleJumpListener(), this);
        getServer().getPluginManager().registerEvents(new StatusGUI(this), this);
        getServer().getPluginManager().registerEvents(new MentionChatListener(), this);

        /* Commands */
        getCommand("swords").setExecutor(new OpenSwordsGUICommand(new SwordsGUI(this)));
        getCommand("status").setExecutor(new OpenStatusGUICommand(new StatusGUI(this)));

    }

    @Override
    public void onDisable() {
        getLogger().info("@ Kayden Plugin disabled");
    }

}