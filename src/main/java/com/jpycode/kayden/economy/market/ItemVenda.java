package com.jpycode.kayden.economy.market;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public record ItemVenda(String vendedor, int preco, ItemStack item, List<String> lore) {
}
