package org.olavorw.lifesteal; 

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit; 


import net.kyori.adventure.text.Component;

import java.util.Date; 

public class PlayerDeathListener implements Listener {

    private final Lifesteal plugin;

    

    public PlayerDeathListener(Lifesteal plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller(); 

        if (killer == null || killer == victim) {
            return; 
        }

        
        AttributeInstance victimMaxHealthAttr = victim.getAttribute(Attribute.MAX_HEALTH);
        if (victimMaxHealthAttr != null) {
            double currentVictimHP = victimMaxHealthAttr.getBaseValue();
            double currentVictimHearts = currentVictimHP / Lifesteal.HP_PER_HEART;

            double newVictimHeartsRaw = currentVictimHearts - 1.0;

            if (newVictimHeartsRaw <= 0.0) {
                String banReason = "You ran out of hearts!";

                victim.ban(banReason, (Date) null, "LifestealPlugin");

                final Component kickMessage = Component.text(banReason);
                plugin.getServer().getScheduler().runTask(plugin, () -> victim.kick(kickMessage));

                plugin.getLogger().info(victim.getName() + " ran out of hearts and has been banned by Lifesteal.");
            } else {
                double newVictimHearts = Math.max(Lifesteal.MIN_POSSIBLE_HEARTS, newVictimHeartsRaw);
                double newVictimHP = newVictimHearts * Lifesteal.HP_PER_HEART;

                victimMaxHealthAttr.setBaseValue(newVictimHP);

                if (victim.getHealth() > newVictimHP) {
                    victim.setHealth(newVictimHP);
                }

                plugin.getLogger().info(victim.getName() + " lost a heart, now has " + newVictimHearts + " hearts (" + newVictimHP + " HP).");
            }
        }

        
        AttributeInstance killerMaxHealthAttr = killer.getAttribute(Attribute.MAX_HEALTH);
        if (killerMaxHealthAttr != null) {
            double currentKillerHP = killerMaxHealthAttr.getBaseValue();
            double currentKillerHearts = currentKillerHP / Lifesteal.HP_PER_HEART;
            double newKillerHearts = Math.min(Lifesteal.MAX_POSSIBLE_HEARTS, currentKillerHearts + 1.0);

            killerMaxHealthAttr.setBaseValue(newKillerHearts * Lifesteal.HP_PER_HEART);
            
            
            plugin.getLogger().info(killer.getName() + " gained a heart from " + victim.getName() + ", now has " + newKillerHearts + " hearts (" + (newKillerHearts * Lifesteal.HP_PER_HEART) + " HP).");
        }
    }
}