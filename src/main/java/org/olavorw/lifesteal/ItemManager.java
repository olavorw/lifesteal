package org.olavorw.lifesteal;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.ChatColor; 

import java.util.Arrays; 

public class ItemManager {

    public static final String HEART_ITEM_NAME = ChatColor.RED + "Heart"; 

    private static Lifesteal plugin;
    private static NamespacedKey HEART_GEN_KEY;

    public static void init(Lifesteal pl) {
        plugin = pl;
        HEART_GEN_KEY = new NamespacedKey(pl, "heart_gen");
    }

    private static int getCurrentGeneration() {
        if (plugin == null) return 1;
        return plugin.getConfig().getInt("heartItemGeneration", 1);
    }

    private static boolean isGenerationRequired() {
        if (plugin == null) return false;
        return plugin.getConfig().getBoolean("requireHeartItemGeneration", false);
    }

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
            // Tag with current generation so future items remain valid after invalidation
            if (HEART_GEN_KEY != null) {
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                pdc.set(HEART_GEN_KEY, PersistentDataType.INTEGER, getCurrentGeneration());
            }
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

    // Returns true only if the stack is a Heart item and passes generation validity rules
    public static boolean isValidHeartItem(ItemStack item) {
        if (!isHeartItem(item)) return false;
        if (plugin == null || HEART_GEN_KEY == null) return true; // fallback

        boolean require = isGenerationRequired();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        Integer gen = pdc.get(HEART_GEN_KEY, PersistentDataType.INTEGER);

        if (!require) {
            // Before invalidation, accept items regardless of tag; but if present, it should match current gen
            if (gen == null) return true;
            return gen == getCurrentGeneration();
        } else {
            // After invalidation, only accept items that explicitly match current generation
            return gen != null && gen == getCurrentGeneration();
        }
    }
}