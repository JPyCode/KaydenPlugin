package com.jpycode.kayden.listeners;

import com.jpycode.kayden.hook.DiscordSRVHook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        /* Checks if user has mentioned on chat and ping him. */
        final String[] message = e.getMessage().split(" ");
        for (int i = 0; i < message.length; i++) {
            final Player p;
            if (message[i].matches("^\\w{3,16}$") && (p = Bukkit.getPlayer(message[i])) != null) {
                message[i] = "§9@" + p.getName() + "§r";
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2f, 0.5f);
            }
        }
        e.setMessage(String.join(" ", message));

        DiscordSRVHook.sendMessage("geral", e.getPlayer().getName() + ": " + e.getMessage());



    }
}
