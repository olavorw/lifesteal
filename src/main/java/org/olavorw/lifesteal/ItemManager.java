package org.olavorw.lifesteal;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor; 

import java.util.Arrays; 

public class ItemManager {

    public static final String HEART_ITEM_NAME = ChatColor.RED + "Heart"; 

    public static ItemStack createHeartItem() {
        ItemStack heartItem = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = heartItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(HEART_ITEM_NAME);
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Right click to use.", 
                    ChatColor.GRAY + "Increases your maximum health.",
                    ChatColor.DARK_GRAY + "This item can be withdrawn after consumption." 
            ));
            heartItem.setItemMeta(meta);
        }
        return heartItem;
    }

    
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
        
        
        return true;
    }
}