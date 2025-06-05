package org.olavorw.lifesteal; // Ensure this matches your package

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit; // Still useful for Bukkit.broadcastMessage or other server-wide actions

// Import for modern text components (for kick messages)
import net.kyori.adventure.text.Component;

import java.util.Date; // Required for the ban method that takes a Date

public class PlayerDeathListener implements Listener {

    private final Lifesteal plugin;

    // Constants for health management

    public PlayerDeathListener(Lifesteal plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller(); // This can be null if death wasn't by a player

        if (killer == null || killer == victim) {
            return; // Only player-on-player kills, no self-kills processed here
        }

        // --- Victim Logic ---
        AttributeInstance victimMaxHealthAttr = victim.getAttribute(Attribute.MAX_HEALTH);
        if (victimMaxHealthAttr != null) {
            double currentVictimHP = victimMaxHealthAttr.getBaseValue();
            double currentVictimHearts = currentVictimHP / Lifesteal.HP_PER_HEART;
            double newVictimHearts = Math.max(Lifesteal.MIN_POSSIBLE_HEARTS, currentVictimHearts - 1.0);

            victimMaxHealthAttr.setBaseValue(newVictimHearts * Lifesteal.HP_PER_HEART);
            // Ensure current health doesn't exceed new max health
            if (victim.getHealth() > newVictimHearts * Lifesteal.HP_PER_HEART) {
                victim.setHealth(newVictimHearts * Lifesteal.HP_PER_HEART);
            }

            plugin.getLogger().info(victim.getName() + " lost a heart, now has " + newVictimHearts + " hearts (" + (newVictimHearts * Lifesteal.HP_PER_HEART) + " HP).");

            // Ban logic: if they were at MIN_POSSIBLE_HEARTS and lost another one
            // (meaning currentVictimHearts was MIN_POSSIBLE_HEARTS before subtraction)
            if (currentVictimHearts <= Lifesteal.MIN_POSSIBLE_HEARTS && newVictimHearts < currentVictimHearts) {
                String banReason = "You ran out of hearts!";

                // Modern way to ban (name ban, no expiration, specify source plugin)
                // Bukkit.banPlayer(victim.getName(), banReason, (Date) null, "LifestealPlugin"); // Alternative
                victim.ban(banReason, (Date) null, "LifestealPlugin"); // Bans the player object

                // Modern way to kick, scheduled to avoid issues during event handling
                final Component kickMessage = Component.text(banReason);
                plugin.getServer().getScheduler().runTask(plugin, () -> victim.kick(kickMessage));

                plugin.getLogger().info(victim.getName() + " ran out of hearts and has been banned by Lifesteal.");
            }
        }

        // --- Killer Logic ---
        AttributeInstance killerMaxHealthAttr = killer.getAttribute(Attribute.MAX_HEALTH);
        if (killerMaxHealthAttr != null) {
            double currentKillerHP = killerMaxHealthAttr.getBaseValue();
            double currentKillerHearts = currentKillerHP / Lifesteal.HP_PER_HEART;
            double newKillerHearts = Math.min(Lifesteal.MAX_POSSIBLE_HEARTS, currentKillerHearts + 1.0);

            killerMaxHealthAttr.setBaseValue(newKillerHearts * Lifesteal.HP_PER_HEART);
            // Optional: Heal the killer to their new max health or by one heart
            // killer.setHealth(newKillerHearts * Lifesteal.HP_PER_HEART); // Heal to new max
            plugin.getLogger().info(killer.getName() + " gained a heart from " + victim.getName() + ", now has " + newKillerHearts + " hearts (" + (newKillerHearts * Lifesteal.HP_PER_HEART) + " HP).");
        }
    }
}