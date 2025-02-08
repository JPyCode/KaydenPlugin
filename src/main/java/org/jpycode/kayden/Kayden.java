package org.jpycode.kayden;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jpycode.kayden.commands.economy.Balance;
import org.jpycode.kayden.commands.economy.MoneyCommand;
import org.jpycode.kayden.commands.status.OpenStatusGUICommand;
import org.jpycode.kayden.commands.swords.OpenSwordsGUICommand;
import org.jpycode.kayden.gui.statusGUI.StatusGUI;
import org.jpycode.kayden.gui.swordsGUI.SwordsGUI;
import org.jpycode.kayden.listeners.DamageTracker;
import org.jpycode.kayden.listeners.KillListener;
import org.jpycode.kayden.listeners.MentionChatListener;
import org.jpycode.kayden.listeners.ThunderSwordListener;
import org.jpycode.kayden.scoreboard.MainScoreboard;

import java.io.File;
import java.util.HashMap;

public final class Kayden extends JavaPlugin {
    private MainScoreboard mainScoreboard;
    private final HashMap<String, Integer> playerKills = new HashMap<>();
    @Getter
    private static Kayden instance;


    @Override
    public void onEnable() {
        instance = this;
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
        getCommand("money").setExecutor(new MoneyCommand());
        getCommand("balance").setExecutor(new Balance());

    }

    @Override
    public void onDisable() {
        getLogger().info("@ Kayden Plugin disabled");
    }

}