package org.jpycode.kayden.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class DamageTracker implements Listener {
    private final Plugin plugin;

    public DamageTracker(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player p) {
            e.getEntity().setMetadata("lastAttacker", new FixedMetadataValue(plugin, p.getName()));
        }
    }
}
