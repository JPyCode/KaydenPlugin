package com.jpycode.kayden.rpg.swords.gui;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import com.jpycode.kayden.rpg.items.weapons.swords.thunder.ThunderSword;

public class SwordsGUI implements Listener {

    private final JavaPlugin plugin;
    private final ThunderSword thunderSword = new ThunderSword();

    public SwordsGUI(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player p) {
        Inventory inventory = plugin.getServer().createInventory(p, 9, Component.text("Swords GUI"));

        inventory.setItem(0, thunderSword.getItemStack());
        p.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws ClassCastException{
        Player p = (Player) e.getWhoClicked();
        String title = ChatColor.stripColor(e.getView().getTitle());
        if (e.getClickedInventory() != null && title.equalsIgnoreCase("Swords GUI")) {
            e.setCancelled(true);
            switch (e.getSlot()) {
                case 0:
                    p.getInventory().addItem(thunderSword.getItemStack());

                    p.sendMessage("You received the Thunder Sword!");
                    p.closeInventory();
                    p.playSound(Sound.sound(
                            org.bukkit.Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
                            Sound.Source.PLAYER,
                            1.0f,
                            1.0f
                    ));
                    break;

//                case 1:
//                    p.getInventory().addItem(flameSword.getItemStack());
//                    p.sendMessage("You received the Flame sword");
//                    p.closeInventory();
//                    break;
                default:

            }
        }
    }
}
