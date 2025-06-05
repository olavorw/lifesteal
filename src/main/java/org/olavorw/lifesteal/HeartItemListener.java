package org.olavorw.lifesteal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.attribute.Attribute; 
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class HeartItemListener implements Listener {

    private final Lifesteal plugin; 

    public HeartItemListener(Lifesteal plugin) {
        this.plugin = plugin; 
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (ItemManager.isHeartItem(itemInHand)) {
                event.setCancelled(true);

                
                AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
                if (maxHealthAttribute == null) {
                    player.sendMessage(ChatColor.RED + "Error: Could not access your health attributes.");
                    
                    plugin.getLogger().severe("Failed to get GENERIC_MAX_HEALTH attribute for player: " + player.getName());
                    return;
                }

                double currentMaxHP = maxHealthAttribute.getBaseValue();
                
                double calculatedNewMaxHP = currentMaxHP + Lifesteal.HP_PER_HEART;

                
                plugin.getLogger().info(player.getName() + " using Heart Item: " +
                        "CurrentMaxHP=" + currentMaxHP + ", " +
                        "HP_PER_HEART_Constant=" + Lifesteal.HP_PER_HEART + ", " +
                        "Calculated NewMaxHP (before cap)=" + calculatedNewMaxHP);

                if (currentMaxHP >= Lifesteal.MAX_POSSIBLE_HEARTS * Lifesteal.HP_PER_HEART) {
                    player.sendMessage(ChatColor.YELLOW + "You are already at the maximum health!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 0.5f);
                    return;
                }

                
                double finalNewMaxHP = Math.min(calculatedNewMaxHP, Lifesteal.MAX_POSSIBLE_HEARTS * Lifesteal.HP_PER_HEART);
                maxHealthAttribute.setBaseValue(finalNewMaxHP);

                plugin.getLogger().info(player.getName() + " successfully updated max HP to: " + finalNewMaxHP);


                itemInHand.setAmount(itemInHand.getAmount() - 1);

                player.sendMessage(ChatColor.GREEN + "Your maximum health has increased!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }
    }
}