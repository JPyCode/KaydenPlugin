package org.jpycode.kayden.items.weapons.swords;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jpycode.kayden.items.base.CustomItem;

import java.util.Arrays;
import java.util.List;

public class ThunderSword extends CustomItem {

    public ThunderSword() {
        super("Thunder Sword", createItem());
    }

    private static ItemStack createItem() {
        int attackDamage = 10;
        int criticalChance = 20;
        List<Component> lore = Arrays.asList(
                Component.text("Channel the fury of storms."),
                Component.text("Critical hits summon lightning."),
                Component.text(""),
                Component.text("Attack damage +" + attackDamage),
                Component.text("Critical chance: " + criticalChance + "%")
        );

        AttributeModifier damageModifier = new AttributeModifier(
                new NamespacedKey(NamespacedKey.BUKKIT, "thunder_sword_damage"),
                50, // Sets sword damage to this value
                AttributeModifier.Operation.ADD_NUMBER
        );

        ItemStack sword = createBaseItem(Material.NETHERITE_SWORD, "Thunder Sword", lore);

        ItemMeta meta = sword.getItemMeta();
        if(meta != null) {
            // Custom Thunder Sword meta
            meta.setCustomModelData(1001);
            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            sword.setItemMeta(meta);
        }

        return sword;
    }
    @Override
    public void onUse(Player player, Event event) {

    }
}
