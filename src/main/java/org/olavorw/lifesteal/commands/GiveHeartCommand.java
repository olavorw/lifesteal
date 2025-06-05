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

public class GiveHeartCommand extends CommandBase {

    public GiveHeartCommand(Lifesteal plugin) {
        super(plugin,
                "giveheart",
                "Increases a player's maximum hearts.",
                "/giveheart <player> [amount]",
                Arrays.asList("gh", "addheart"),
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

        int heartsToAdd = 1;
        if (args.length == 2) {
            try {
                heartsToAdd = Integer.parseInt(args[1]);
                if (heartsToAdd <= 0) {
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

        if (currentHearts >= Lifesteal.MAX_POSSIBLE_HEARTS) {
            sender.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " is already at the maximum number of hearts (" + (int)Lifesteal.MAX_POSSIBLE_HEARTS + ").");
            return true;
        }

        double newHearts = Math.min(Lifesteal.MAX_POSSIBLE_HEARTS, currentHearts + heartsToAdd);
        double newMaxHP = newHearts * Lifesteal.HP_PER_HEART;

        maxHealthAttribute.setBaseValue(newMaxHP);

        sender.sendMessage(ChatColor.GREEN + "Gave " + heartsToAdd + " heart(s) to " + targetPlayer.getName() + ". They now have " + (int)newHearts + " hearts.");
        targetPlayer.sendMessage(ChatColor.GREEN + "Your maximum health has been increased by " + heartsToAdd + " heart(s)!");
        plugin.getLogger().info(sender.getName() + " gave " + heartsToAdd + " heart(s) to " + targetPlayer.getName() + ". New max HP: " + newMaxHP);

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