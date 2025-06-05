package org.olavorw.lifesteal.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.olavorw.lifesteal.Lifesteal;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;

public class CheckHealthCommand extends CommandBase {

    public CheckHealthCommand(Lifesteal plugin) {
        super(plugin,
                "checkhealth",
                "Checks a player's current health and maximum hearts.",
                "/checkhealth <player>",
                Collections.singletonList("ch"),
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

        double currentHealth = targetPlayer.getHealth();
        double maxHearts = targetPlayer.getAttribute(Attribute.MAX_HEALTH).getValue() / Lifesteal.HP_PER_HEART;

        sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + "'s Health: " + currentHealth + " HP (" + maxHearts + " Hearts)");
        return true;
    }
}
