package com.jpycode.kayden.economy.market;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.jpycode.kayden.economy.database.MarketItem.saveMarketItem;


public class MarketPriceGUI implements Listener {
    private static final String GUI_TITLE = ChatColor.GREEN + "Definir preço";
    private static final Map<Player, MarketItem> playerMarketItems = new HashMap<>();

    public static void openPriceGUI(Player player, ItemStack item, List<String> lore) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        MarketItem marketItem = new MarketItem(item, 100, lore);
        playerMarketItems.put(player, marketItem);
        updateGUI(gui, marketItem);
        player.openInventory(gui);
    }

    private static void updateGUI(Inventory gui, MarketItem marketItem) {
        gui.clear();

        ItemStack itemForSale = marketItem.getItem().clone();
        ItemMeta meta = itemForSale.getItemMeta();
        List<String> lore = new ArrayList<>(marketItem.getLore());
        System.out.println("Lore: " + marketItem.getLore());
        System.out.println("Price: " + marketItem.getPrice());
        if (lore == null) {
            lore = Collections.singletonList(ChatColor.YELLOW + "Preço: " + ChatColor.GOLD + "$" + marketItem.getPrice());
        } else {
            lore.add(ChatColor.YELLOW + "Preço: " + ChatColor.GOLD + "$" + marketItem.getPrice());
        }
        meta.setLore(lore);
        lore.clear();
        itemForSale.setItemMeta(meta);

        gui.setItem(13, itemForSale); // Item central
        gui.setItem(10, createButton(Material.REDSTONE, "-1"));
        gui.setItem(11, createButton(Material.REDSTONE, "-10"));
        gui.setItem(12, createButton(Material.REDSTONE, "-100"));
        gui.setItem(14, createButton(Material.EMERALD, "+1"));
        gui.setItem(15, createButton(Material.EMERALD, "+10"));
        gui.setItem(16, createButton(Material.EMERALD, "+100"));
        gui.setItem(22, createButton(Material.LIME_WOOL, "Confirmar"));
    }

    private static ItemStack createButton(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!e.getView().getTitle().equals(GUI_TITLE)) return;
        e.setCancelled(true);

        MarketItem marketItem = playerMarketItems.get(player);
        if (marketItem == null) return;
        System.out.println(marketItem.getLore());

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = clickedItem.getItemMeta().getDisplayName();
        int priceChange = 0;
        switch (ChatColor.stripColor(itemName)) {
            case "-1":
                priceChange = -1;
                break;
            case "-10":
                priceChange = -10;
                break;
            case "-100":
                priceChange = -100;
                break;
            case "+1":
                priceChange = 1;
                break;
            case "+10":
                priceChange = 10;
                break;
            case "+100":
                priceChange = 100;
                break;
            case "Confirmar":
                player.sendMessage(ChatColor.GREEN + "Item listado por " + ChatColor.GOLD + "$" + marketItem.getPrice());
                System.out.println("Lore depois do click: " + marketItem.getLore());
                saveMarketItem(player, marketItem.getItem(), marketItem.getPrice(), marketItem.getLore());
                player.closeInventory();
                return;
        }

        marketItem.setPrice(Math.max(0, marketItem.getPrice() + priceChange));
        System.out.println(marketItem.getPrice() + ", " + priceChange);
        updateGUI(e.getInventory(), marketItem);
    }


    public static String serializeItemStack(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            dataOutput.flush();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ItemStack deserializeItemStack(String data) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            return (ItemStack) dataInput.readObject();

        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
