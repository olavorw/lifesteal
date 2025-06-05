package org.olavorw.lifesteal.commands;

import org.olavorw.lifesteal.Lifesteal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;

public class ResetPlayerHeartsCommand extends CommandBase {
    public ResetPlayerHeartsCommand(Lifesteal plugin) {
        super(plugin,
                "resethearts",
                "Resets a player's maximum hearts to the default value.",
                "/resethearts <player>",
                Collections.singletonList("rh"),
                "lifesteal.admin"
        );
    }
    @Override
    public boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage());
            return false;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found or is not online.");
            return true;
        }

        double defaultMaxHP = Lifesteal.DEFAULT_PLAYER_HEARTS * Lifesteal.HP_PER_HEART;
        AttributeInstance maxHealthAttribute = targetPlayer.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttribute == null) {
            sender.sendMessage(ChatColor.RED + "Error: Could not access health attributes for " + targetPlayer.getName() + ".");
            plugin.getLogger().severe("Failed to get GENERIC_MAX_HEALTH attribute for player: " + targetPlayer.getName());
            return true;
        }

        maxHealthAttribute.setBaseValue(defaultMaxHP);
        targetPlayer.setHealth(defaultMaxHP); 
        targetPlayer.sendMessage(ChatColor.YELLOW + "Your maximum hearts have been reset by a server administrator.");

        sender.sendMessage(ChatColor.GREEN + "Reset maximum hearts for " + targetPlayer.getName() + " to default (" + (int)Lifesteal.DEFAULT_PLAYER_HEARTS + " hearts).");
        plugin.getLogger().info("Reset max HP for player: " + targetPlayer.getName());

        return true;
    }
}
