package org.jpycode.kayden;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jpycode.kayden.commands.OpenStatusGUICommand;
import org.jpycode.kayden.commands.OpenSwordsGUICommand;
import org.jpycode.kayden.gui.statusGUI.StatusGUI;
import org.jpycode.kayden.gui.swordsGUI.SwordsGUI;
import org.jpycode.kayden.listeners.DamageTracker;
import org.jpycode.kayden.listeners.KillListener;
import org.jpycode.kayden.listeners.MentionChatListener;
import org.jpycode.kayden.listeners.ThunderSwordListener;
import org.jpycode.kayden.scoreboard.MainScoreboard;

import java.util.HashMap;

public final class Kayden extends JavaPlugin {
    private MainScoreboard mainScoreboard;
    private final HashMap<String, Integer> playerKills = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("@ Kayden Plugin enabled");
        mainScoreboard = new MainScoreboard(this, playerKills);


        /* Listeners */
        getServer().getPluginManager().registerEvents(new SwordsGUI(this), this);
        getServer().getPluginManager().registerEvents(new ThunderSwordListener(), this);
        getServer().getPluginManager().registerEvents(new StatusGUI(this), this);
        getServer().getPluginManager().registerEvents(new MentionChatListener(), this);
        getServer().getPluginManager().registerEvents(new KillListener(playerKills, mainScoreboard), this);
        getServer().getPluginManager().registerEvents(new DamageTracker(this), this);

        /* Commands */
        getCommand("swords").setExecutor(new OpenSwordsGUICommand(new SwordsGUI(this)));
        getCommand("status").setExecutor(new OpenStatusGUICommand(new StatusGUI(this)));

    }

    @Override
    public void onDisable() {
        getLogger().info("@ Kayden Plugin disabled");
    }

}