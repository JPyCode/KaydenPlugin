package org.jpycode.kayden.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpListener implements Listener {
    @EventHandler
    public void onPlayerDoubleJump(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();

        if(p.getGameMode() != GameMode.SURVIVAL) return;

        if(!p.isFlying()) {
            e.setCancelled(true);
            p.setAllowFlight(false);
            p.setVelocity(p.getLocation().getDirection().setY(0.5).multiply(1));
        }
    }

    @EventHandler
    public void onPlayerLand(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(p.isOnGround()) {
            p.setAllowFlight(true);
        }
    }
}
