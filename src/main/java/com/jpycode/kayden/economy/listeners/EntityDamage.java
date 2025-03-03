package com.jpycode.kayden.economy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamage implements Listener {

    @EventHandler
    public void playerHit(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player) {
            System.out.println(event.getDamage());
        }
    }
}
