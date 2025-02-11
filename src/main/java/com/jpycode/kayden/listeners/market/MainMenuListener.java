package com.jpycode.kayden.listeners.market;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainMenuListener implements Listener {

    @EventHandler
    public void onPlayerClickGUI(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        switch (e.getView().getTitle().toLowerCase()) {
            case "market menu" -> {
                e.setCancelled(true);
                System.out.println(e.getSlot());
            }
        }
    }
}
