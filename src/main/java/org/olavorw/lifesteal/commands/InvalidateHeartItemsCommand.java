package org.olavorw.lifesteal.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.olavorw.lifesteal.Lifesteal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvalidateHeartItemsCommand extends CommandBase {

    public InvalidateHeartItemsCommand(Lifesteal plugin) {
        super(plugin,
                "invalidateheartitems",
                "Invalidates all existing Heart items; future crafted/given ones remain valid.",
                "/invalidateheartitems",
                Arrays.asList("invalidatehearts", "ihearts"),
                "lifesteal.admin"
        );
    }

    @Override
    public boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // Increment generation and require matching generation for consumption
        int currentGen = plugin.getConfig().getInt("heartItemGeneration", 1);
        int newGen = currentGen + 1;
        plugin.getConfig().set("heartItemGeneration", newGen);
        plugin.getConfig().set("requireHeartItemGeneration", true);
        plugin.saveConfig();

        // Re-register the crafting recipe so it produces items tagged with the new generation
        NamespacedKey recipeKey = new NamespacedKey(plugin, "custom_heart");
        try {
            Bukkit.removeRecipe(recipeKey);
        } catch (Throwable ignored) {
            // In case server version doesn't support removeRecipe or throws; we'll try to re-add regardless
        }
        plugin.registerHeartRecipe();

        sender.sendMessage(ChatColor.GREEN + "All existing Heart items have been invalidated. New Heart items will be valid (generation " + newGen + ").");
        plugin.getLogger().info(sender.getName() + " invalidated all current Heart items. Generation advanced from " + currentGen + " to " + newGen + ".");
        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission(this.getPermission())) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
}
