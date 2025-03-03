package com.jpycode.kayden;

import com.jpycode.kayden.economy.commands.Eco;
import com.jpycode.kayden.economy.commands.Money;
import com.jpycode.kayden.economy.commands.Pay;
import com.jpycode.kayden.economy.listeners.EntityDamage;
import com.jpycode.kayden.economy.listeners.shop.InventoryClickListener;
import com.jpycode.kayden.economy.listeners.MarketBuyListener;
import com.jpycode.kayden.economy.gui.MarketPriceGUI;
import com.jpycode.kayden.economy.market.Market;
import com.jpycode.kayden.scoreboard.MainScoreboard;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.jpycode.kayden.rpg.status.OpenStatusGUICommand;
import com.jpycode.kayden.rpg.swords.OpenSwordsGUICommand;
import com.jpycode.kayden.database.Database;
import com.jpycode.kayden.rpg.status.gui.StatusGUI;
import com.jpycode.kayden.rpg.swords.gui.SwordsGUI;
import com.jpycode.kayden.listeners.KillListener;
import com.jpycode.kayden.listeners.ChatListener;
import com.jpycode.kayden.rpg.items.weapons.swords.thunder.ThunderSwordListener;

import java.io.File;


public final class Kayden extends JavaPlugin {
    @Getter
    private static Kayden instance;
    private MainScoreboard scoreboard;

    @Override
    public void onEnable() {
        instance = this;
        Database.connect();

        saveDefaultFile("config.yml");
        saveDefaultFile("shop.yml");

        this.scoreboard = new MainScoreboard(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboard.updateScoreboard(player);
        }


        /* Listeners */
        getServer().getPluginManager().registerEvents(new SwordsGUI(this), this);
        getServer().getPluginManager().registerEvents(new ThunderSwordListener(), this);
        getServer().getPluginManager().registerEvents(new StatusGUI(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new KillListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        getServer().getPluginManager().registerEvents(new MarketPriceGUI(), this);
        getServer().getPluginManager().registerEvents(new Market(), this);
        getServer().getPluginManager().registerEvents(new MarketBuyListener(), this);


        /* Commands */
        getCommand("swords").setExecutor(new OpenSwordsGUICommand(new SwordsGUI(this)));
        getCommand("status").setExecutor(new OpenStatusGUICommand(new StatusGUI()));

        // Economy
        getCommand("money").setExecutor(new Money());
        getCommand("pay").setExecutor(new Pay());
        getCommand("eco").setExecutor(new Eco());
        getCommand("market").setExecutor(new Market());



        getLogger().info("@ Kayden Plugin enabled");
    }

    @Override
    public void onDisable() {

        Database.close();

        getLogger().info("@ Kayden Plugin disabled");
    }

    private void saveDefaultFile(String fileName) {
        File file = new File(getDataFolder(), fileName);

        if (!file.exists()) {
            getLogger().info(fileName + " não encontrado! Criando...");
            saveResource(fileName, false);
        }
    }

}