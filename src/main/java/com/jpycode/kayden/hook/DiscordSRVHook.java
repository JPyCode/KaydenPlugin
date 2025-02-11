package com.jpycode.kayden.hook;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
                + " &f>> " + message
        ));
    }

    public static void sendMessage(String channel, String message) {
        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channel);
        if(textChannel != null) textChannel.sendMessage(message).complete();
    }
    public static void register() {
        DiscordSRV.api.subscribe(instance);
    }

    public static void unregister() {
        DiscordSRV.api.subscribe(instance);
    }
}
