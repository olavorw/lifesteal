package org.olavorw.lifesteal.commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.olavorw.lifesteal.ItemManager; // Your ItemManager class
import org.olavorw.lifesteal.Lifesteal;   // Your main plugin class
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WithdrawHeartCommand extends CommandBase {

    public WithdrawHeartCommand(Lifesteal plugin) {
        super(plugin,
                "withdrawheart",
                "Withdraws hearts from your health bar into Heart items.",
                "/withdrawheart [amount]",
                Arrays.asList("wh", "takemyheart"), // Aliases
                "lifesteal.player.withdraw" // Permission (default: true from paper-plugin.yml)
        );
    }

    @Override
    public boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttribute == null) {
            player.sendMessage(ChatColor.RED + "Error: Could not access your health attributes.");
            plugin.getLogger().severe("Failed to get GENERIC_MAX_HEALTH attribute for player: " + player.getName());
            return true;
        }

        double currentMaxHP = maxHealthAttribute.getBaseValue();
        double currentHearts = currentMaxHP / Lifesteal.HP_PER_HEART;

        // Calculate how many hearts can actually be withdrawn
        double withdrawableHeartsDouble = currentHearts - Lifesteal.MIN_POSSIBLE_HEARTS;
        if (withdrawableHeartsDouble < 1.0) { // Cannot withdraw if it would take them to or below min
            player.sendMessage(ChatColor.RED + "You do not have enough hearts to withdraw. You need to be above " + (int)Lifesteal.MIN_POSSIBLE_HEARTS + " heart(s).");
            return true;
        }
        int maxWithdrawableHearts = (int) Math.floor(withdrawableHeartsDouble);


        int heartsToWithdraw = 1; // Default to 1
        if (args.length > 0) {
            try {
                heartsToWithdraw = Integer.parseInt(args[0]);
                if (heartsToWithdraw <= 0) {
                    player.sendMessage(ChatColor.RED + "Amount of hearts to withdraw must be a positive number.");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid amount. Please enter a number.");
                return true;
            }
        }

        if (heartsToWithdraw > maxWithdrawableHearts) {
            player.sendMessage(ChatColor.RED + "You can only withdraw a maximum of " + maxWithdrawableHearts + " heart(s) at this time.");
            return true;
        }

        // Proceed with withdrawal
        double newHearts = currentHearts - heartsToWithdraw;
        double newMaxHP = newHearts * Lifesteal.HP_PER_HEART;
        maxHealthAttribute.setBaseValue(newMaxHP);

        // Adjust current health if it's higher than the new max health
        if (player.getHealth() > newMaxHP) {
            player.setHealth(newMaxHP);
        }

        ItemStack heartItemStack = ItemManager.createHeartItem();
        heartItemStack.setAmount(heartsToWithdraw);

        Map<Integer, ItemStack> leftover = player.getInventory().addItem(heartItemStack);
        if (!leftover.isEmpty()) {
            for (ItemStack item : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
            player.sendMessage(ChatColor.YELLOW + "Your inventory was full, so some heart items were dropped on the ground!");
        }

        player.sendMessage(ChatColor.GREEN + "You have withdrawn " + heartsToWithdraw + " heart(s). Your maximum health is now " + (int)newHearts + " hearts.");
        plugin.getLogger().info(player.getName() + " withdrew " + heartsToWithdraw + " heart(s). New max HP: " + newMaxHP);

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!(sender instanceof Player) || !sender.hasPermission(this.getPermission())) {
            return Collections.emptyList();
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealthAttribute != null) {
                double currentHearts = maxHealthAttribute.getBaseValue() / Lifesteal.HP_PER_HEART;
                double withdrawableHeartsDouble = currentHearts - Lifesteal.MIN_POSSIBLE_HEARTS;
                int maxWithdrawableHearts = (int) Math.floor(withdrawableHeartsDouble);
                if (maxWithdrawableHearts >= 1) {
                    return Arrays.asList("1", String.valueOf(maxWithdrawableHearts));
                }
            }
            return Collections.singletonList("1"); // Default suggestion
        }
        return Collections.emptyList();
    }
}