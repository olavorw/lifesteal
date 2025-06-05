package org.olavorw.lifesteal.commands;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.olavorw.lifesteal.Lifesteal; 
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RevivePlayerCommand extends CommandBase {

    public RevivePlayerCommand(Lifesteal plugin) {
        super(plugin,
                "reviveplayer",
                "Unbans a player and resets their hearts to default.",
                "/reviveplayer <player>",
                Arrays.asList("lifestealrevive", "lrevive"), 
                "lifesteal.admin" 
        );
    }

    @Override
    public boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage());
            return false;
        }

        String targetName = args[0];
        OfflinePlayer targetOfflinePlayer = Bukkit.getOfflinePlayer(targetName); 

        
        BanList nameBanList = Bukkit.getBanList(BanList.Type.NAME);
        if (nameBanList.isBanned(targetName)) {
            nameBanList.pardon(targetName);
            sender.sendMessage(ChatColor.GREEN + targetName + " has been unbanned.");
            plugin.getLogger().info(sender.getName() + " unbanned " + targetName + " using /reviveplayer.");
        } else {
            sender.sendMessage(ChatColor.YELLOW + targetName + " is not currently name-banned by the server.");
            
        }

        
        double defaultMaxHP = Lifesteal.DEFAULT_PLAYER_HEARTS * Lifesteal.HP_PER_HEART;

        if (targetOfflinePlayer.isOnline()) {
            Player targetOnlinePlayer = targetOfflinePlayer.getPlayer();
            if (targetOnlinePlayer != null) {
                AttributeInstance maxHealthAttribute = targetOnlinePlayer.getAttribute(Attribute.MAX_HEALTH);
                if (maxHealthAttribute != null) {
                    maxHealthAttribute.setBaseValue(defaultMaxHP);
                    targetOnlinePlayer.setHealth(defaultMaxHP); 
                    sender.sendMessage(ChatColor.GREEN + targetOnlinePlayer.getName() + "'s hearts have been reset to " + (int)Lifesteal.DEFAULT_PLAYER_HEARTS + ".");
                    targetOnlinePlayer.sendMessage(ChatColor.GREEN + "You have been revived and your hearts reset!");
                    plugin.getLogger().info(targetOnlinePlayer.getName() + "'s max HP set to " + defaultMaxHP + " by /reviveplayer.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Could not modify health attributes for " + targetOnlinePlayer.getName() + ".");
                }
            }
        } else {
            
            
            sender.sendMessage(ChatColor.YELLOW + targetName + " is offline. Their hearts will be reset to default upon their next login once data persistence is implemented.");
            plugin.getLogger().info(targetName + " is offline. Their lifesteal data should be reset for next login (requires persistence).");
            
            
        }

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
        return Collections.emptyList();
    }
}