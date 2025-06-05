package org.olavorw.lifesteal.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.olavorw.lifesteal.ItemManager;
import org.olavorw.lifesteal.Lifesteal;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GiveHeartItemCommand extends CommandBase {

    public GiveHeartItemCommand(Lifesteal plugin) {
        super(plugin, // Plugin instance
                "giveheartitem", // Command name
                "Gives a player the custom Heart item.", // Description
                "/giveheartitem <player> [amount]", // Usage message
                Arrays.asList("ghi"), // Aliases
                "lifesteal.admin" // Permission
        );
    }

    @Override
    public boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // Permission check is handled by CommandBase.execute

        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage()); // getUsage() from Command class
            return false; // Returning false typically shows the usage message
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found or is not online.");
            return true;
        }

        int amount = 1;
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    sender.sendMessage(ChatColor.RED + "Amount must be a positive number.");
                    return true;
                }
                if (amount > 64) { // Max stack size, can be adjusted
                    sender.sendMessage(ChatColor.RED + "You can only give up to 64 items at a time.");
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount. Please enter a number.");
                return true;
            }
        }

        ItemStack heartItemStack = ItemManager.createHeartItem();
        heartItemStack.setAmount(amount);

        Map<Integer, ItemStack> leftover = targetPlayer.getInventory().addItem(heartItemStack);

        if (!leftover.isEmpty()) {
            for (ItemStack item : leftover.values()) {
                targetPlayer.getWorld().dropItemNaturally(targetPlayer.getLocation(), item);
            }
            targetPlayer.sendMessage(ChatColor.YELLOW + "Your inventory was full, so some items were dropped on the ground!");
        }

        sender.sendMessage(ChatColor.GREEN + "Gave " + amount + " Heart item(s) to " + targetPlayer.getName() + ".");
        targetPlayer.sendMessage(ChatColor.GREEN + "You have received " + amount + " Heart item(s)!");
        plugin.getLogger().info(sender.getName() + " gave " + amount + " Heart item(s) to " + targetPlayer.getName());

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission("lifesteal.admin")) { // Also check permission for tab complete
            return Collections.emptyList();
        }
        if (args.length == 1) {
            // Suggest online player names (Bukkit.getOnlinePlayers()... or let default handle)
            return super.tabComplete(sender, alias, args); // Bukkit's default often handles player names
        }
        if (args.length == 2) {
            return Arrays.asList("1", "5", "10"); // Suggest some amounts
        }
        return Collections.emptyList();
    }
}