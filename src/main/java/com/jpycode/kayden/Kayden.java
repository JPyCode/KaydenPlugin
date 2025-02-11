package com.jpycode.kayden;

import com.jpycode.kayden.hook.DiscordSRVHook;
import com.jpycode.kayden.listeners.market.MainMenuListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import com.jpycode.kayden.commands.crate.CrateCommand;
import com.jpycode.kayden.commands.economy.Eco;
import com.jpycode.kayden.commands.economy.MarketCommand;
import com.jpycode.kayden.commands.economy.Money;
import com.jpycode.kayden.commands.economy.Pay;
import com.jpycode.kayden.commands.status.OpenStatusGUICommand;
import com.jpycode.kayden.commands.swords.OpenSwordsGUICommand;
import com.jpycode.kayden.database.Database;
import com.jpycode.kayden.gui.marketGUI.MarketMenu;
import com.jpycode.kayden.gui.statusGUI.StatusGUI;
import com.jpycode.kayden.gui.swordsGUI.SwordsGUI;
import com.jpycode.kayden.listeners.KillListener;
import com.jpycode.kayden.listeners.ChatListener;
import com.jpycode.kayden.listeners.PlayerJoinListener;
import com.jpycode.kayden.listeners.ThunderSwordListener;
import com.jpycode.kayden.managers.CrateManager;
import com.jpycode.kayden.scoreboard.MainScoreboard;

public final class Kayden extends JavaPlugin {
    private MainScoreboard mainScoreboard;
    @Getter
    private static Kayden instance;
    private final Database database = new Database(this);
    private CrateManager crateManager;


    @Override
    public void onEnable() {
        database.connect()
                .thenRun(() -> getLogger().info("Conexão com o banco de dados estabelecida com sucesso!"))
                .exceptionally(ex -> {
                    getLogger().severe("Erro: " + ex.getMessage());
                    return null;
                });
        instance = this;
        mainScoreboard = new MainScoreboard(this);



        /* Listeners */
        getServer().getPluginManager().registerEvents(new SwordsGUI(this), this);
        getServer().getPluginManager().registerEvents(new ThunderSwordListener(), this);
        getServer().getPluginManager().registerEvents(new StatusGUI(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new KillListener(mainScoreboard, this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new MainMenuListener(), this);


        /* Commands */
        getCommand("swords").setExecutor(new OpenSwordsGUICommand(new SwordsGUI(this)));
        getCommand("status").setExecutor(new OpenStatusGUICommand(new StatusGUI(this)));
        getCommand("money").setExecutor(new Money());
        getCommand("pay").setExecutor(new Pay());
        getCommand("eco").setExecutor(new Eco());
        getCommand("crates").setExecutor(new CrateCommand(crateManager));
        getCommand("market").setExecutor(new MarketCommand(new MarketMenu()));





        if(getServer().getPluginManager().getPlugin("DiscordSRV") != null)
            DiscordSRVHook.register();


        getLogger().info("@ Kayden Plugin enabled");
    }

    @Override
    public void onDisable() {
        if(getServer().getPluginManager().getPlugin("DiscordSRV") != null)
            DiscordSRVHook.unregister();



        getLogger().info("@ Kayden Plugin disabled");
    }

}