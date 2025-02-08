package com.jpycode.kayden.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class ThunderSwordListener implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
            if(e.getDamager() instanceof Player p) {
                System.out.println("Henji damaged " + e.getEntity().getName() + ": " + e.getDamage());
                ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                if(meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1001) {
                    Random rand = new Random();
                    int rate = rand.nextInt(100) + 1;
                    System.out.println(rate);
                    if (rate <= 25) {
                        e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
                        p.sendMessage(Component.text("You have summoned a thunderstorm!").color(NamedTextColor.DARK_PURPLE));
                    }
                }
            }
    }
}
