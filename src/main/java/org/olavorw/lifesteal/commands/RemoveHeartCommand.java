package org.olavorw.lifesteal.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.olavorw.lifesteal.Lifesteal; 
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RemoveHeartCommand extends CommandBase {

    public RemoveHeartCommand(Lifesteal plugin) {
        super(plugin,
                "removeheart",
                "Decreases a player's maximum hearts.",
                "/removeheart <player> [amount]",
                Arrays.asList("rh", "takeheart"),
                "lifesteal.admin"
        );
    }

    @Override
    public boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage());
            return false;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found or is not online.");
            return true;
        }

        int heartsToRemove = 1;
        if (args.length == 2) {
            try {
                heartsToRemove = Integer.parseInt(args[1]);
                if (heartsToRemove <= 0) {
                    sender.sendMessage(ChatColor.RED + "Amount of hearts must be a positive number.");
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount. Please enter a number for hearts.");
                return true;
            }
        }

        AttributeInstance maxHealthAttribute = targetPlayer.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttribute == null) {
            sender.sendMessage(ChatColor.RED + "Error: Could not access health attributes for " + targetPlayer.getName() + ".");
            plugin.getLogger().severe("Failed to get GENERIC_MAX_HEALTH attribute for player: " + targetPlayer.getName());
            return true;
        }

        double currentMaxHP = maxHealthAttribute.getBaseValue();
        double currentHearts = currentMaxHP / Lifesteal.HP_PER_HEART;

        if (currentHearts <= Lifesteal.MIN_POSSIBLE_HEARTS) {
            sender.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " is already at the minimum number of hearts (" + (int)Lifesteal.MIN_POSSIBLE_HEARTS + ").");
            return true;
        }

        double newHearts = Math.max(Lifesteal.MIN_POSSIBLE_HEARTS, currentHearts - heartsToRemove);
        double newMaxHP = newHearts * Lifesteal.HP_PER_HEART;

        maxHealthAttribute.setBaseValue(newMaxHP);

        sender.sendMessage(ChatColor.GREEN + "Removed " + heartsToRemove + " heart(s) from " + targetPlayer.getName() + ". They now have " + (int)newHearts + " hearts.");
        targetPlayer.sendMessage(ChatColor.RED + "Your maximum health has been decreased by " + heartsToRemove + " heart(s)!");
        plugin.getLogger().info(sender.getName() + " removed " + heartsToRemove + " heart(s) from " + targetPlayer.getName() + ". New max HP: " + newMaxHP);

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission(this.getPermission())) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            return null; 
        }
        if (args.length == 2) {
            return Arrays.asList("1", "2", "5"); 
        }
        return Collections.emptyList();
    }
}