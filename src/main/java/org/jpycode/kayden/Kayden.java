package org.jpycode.kayden;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jpycode.kayden.scoreboard.Board;

public final class Kayden extends JavaPlugin {
    private BukkitTask task;

    @Override
    public void onEnable() {
        getLogger().info("@ Kayden Plugin enabled");
        task = getServer().getScheduler().runTask(this, Board.getInstance());


    }

    @Override
    public void onDisable() {
        getLogger().info("@ Kayden Plugin disabled");
    }
}