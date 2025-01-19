package org.jpycode.kayden.gui;

import com.google.j2objc.annotations.Property;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.sisu.bean.IgnoreSetters;
import org.jpycode.kayden.items.weapons.ThunderSword;

import java.util.HashMap;
import java.util.Map;

public class SwordsGUI implements Listener {

    private final JavaPlugin plugin;
    private final ThunderSword thunderSword = new ThunderSword();
    private final Map<Player, Inventory> inventories;

    public SwordsGUI(JavaPlugin plugin) {
        this.plugin = plugin;
        this.inventories = new HashMap<>();
    }

    public void openGUI(Player p) {
        Inventory inventory = plugin.getServer().createInventory(p, 9, Component.text("Swords GUI"));

        inventory.setItem(0, thunderSword.getItemStack());
        inventories.put(p, inventory);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws ClassCastException{
        if(!(e.getWhoClicked() instanceof Player p)) return;
        Component inventoryTitleComponent = e.getView().title();

        //Access component content without having to cast directly
        //I suffered a little here...
        String title = inventoryTitleComponent instanceof TextComponent
                ? ((TextComponent) inventoryTitleComponent).content()
                : inventoryTitleComponent.toString();

        if (title.equalsIgnoreCase("Swords GUI")) {
            e.setCancelled(true);
            switch (e.getSlot()) {
                case 0:
                    p.getInventory().addItem(thunderSword.getItemStack());
                    p.sendMessage("You received the Thunder Sword!");
                    p.closeInventory();
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
