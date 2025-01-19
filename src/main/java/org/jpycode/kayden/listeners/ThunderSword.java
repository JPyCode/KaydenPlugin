package org.jpycode.kayden.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class ThunderSword implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
            if(e.getDamager() instanceof Player p) {
                System.out.println("Dano causado: " + e.getDamage());
                ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                if(meta != null && meta.getCustomModelData() == 1001) {
                    e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
                    p.sendMessage(Component.text("You have summoned a thunderstorm!").color(NamedTextColor.DARK_PURPLE));
                }
        }
    }
}
