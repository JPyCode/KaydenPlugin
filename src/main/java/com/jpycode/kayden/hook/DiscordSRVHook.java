package com.jpycode.kayden.hook;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DiscordSRVHook {
    private static final DiscordSRVHook instance = new DiscordSRVHook();

    private DiscordSRVHook() {

    }

    @Subscribe
    public void onMessageReceived(DiscordGuildMessageReceivedEvent event) {
        String playerName = event.getMember().getEffectiveName();
        String channel = event.getChannel().getName();
        String message = event.getMessage().getContentRaw();

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(
                '&', "&7[&bDiscord&7] &f" + playerName
                + " &7in &f" + channel + " &8>> &f" + message
        ));
    }
    public static void register() {
        DiscordSRV.api.subscribe(instance);
    }

    public static void unregister() {
        DiscordSRV.api.subscribe(instance);
    }
}
