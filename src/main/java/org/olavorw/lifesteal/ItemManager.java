package org.olavorw.lifesteal;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor; // For colors

import java.util.Arrays; // For List creation

public class ItemManager {

    public static final String HEART_ITEM_NAME = ChatColor.RED + "Heart"; // Red "Heart"

    public static ItemStack createHeartItem() {
        ItemStack heartItem = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = heartItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(HEART_ITEM_NAME);
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Right click to use.", // Gray lore
                    ChatColor.GRAY + "Increases your maximum health.",
                    ChatColor.DARK_GRAY + "This item can be withdrawn after consumption." // Darker gray for this specific line
            ));
            heartItem.setItemMeta(meta);
        }
        return heartItem;
    }

    // Helper method to check if an ItemStack is our custom Heart item
    public static boolean isHeartItem(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) {
            return false;
        }
        if (!item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() || !meta.getDisplayName().equals(HEART_ITEM_NAME)) {
            return false;
        }
        // Optionally, you can also check for specific lore if you want to be more strict
        // For now, checking name and material should be sufficient for most cases.
        return true;
    }
}