package com.jpycode.kayden.economy.market;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MarketItem {
    @Getter
    private final ItemStack item;
    @Getter
    @Setter
    private int price;
    @Setter
    @Getter
    private List<String> lore;

    public MarketItem(ItemStack item, int price, List<String> lore) {
        this.item = item;
        this.price = price;
        this.lore = lore;
    }
}
