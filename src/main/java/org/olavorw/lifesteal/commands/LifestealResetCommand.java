package org.olavorw.lifesteal.commands;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.olavorw.lifesteal.Lifesteal; // Your main plugin class
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LifestealResetCommand extends CommandBase {

    private static final String BAN_SOURCE = "LifestealPlugin"; // Source used when banning players

    public LifestealResetCommand(Lifesteal plugin) {
        super(plugin,
                "lifestealreset",
                "Resets Lifesteal health data for online players and unbans Lifesteal bans.",
                "/lifestealreset <CONFIRM>",
                Collections.emptyList(), // No aliases for such a command is safer
                "lifesteal.admin"
        );
    }

    @Override
    public boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("CONFIRM")) {
            sender.sendMessage(ChatColor.RED + "This is a dangerous command that will reset Lifesteal data.");
            sender.sendMessage(ChatColor.RED + "Please type " + ChatColor.YELLOW + "/" + commandLabel + " CONFIRM" + ChatColor.RED + " to proceed.");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Starting Lifesteal data reset...");
        plugin.getLogger().info(sender.getName() + " initiated Lifesteal server reset.");

        // 1. Reset health for all ONLINE players
        double defaultMaxHP = Lifesteal.DEFAULT_PLAYER_HEARTS * Lifesteal.HP_PER_HEART;
        int onlinePlayersReset = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            AttributeInstance maxHealthAttribute = onlinePlayer.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealthAttribute != null) {
                maxHealthAttribute.setBaseValue(defaultMaxHP);
                onlinePlayer.setHealth(defaultMaxHP); // Heal them to full
                onlinePlayer.sendMessage(ChatColor.YELLOW + "Your maximum hearts have been reset by a server administrator.");
                onlinePlayersReset++;
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Reset maximum hearts for " + onlinePlayersReset + " online player(s) to default (" + (int)Lifesteal.DEFAULT_PLAYER_HEARTS + " hearts).");
        plugin.getLogger().info("Reset max HP for " + onlinePlayersReset + " online players.");

        // 2. Unban players banned by LifestealPlugin
        BanList nameBanList = Bukkit.getBanList(BanList.Type.NAME);
        Set<BanEntry<?>> banEntries = nameBanList.getBanEntries(); // Use wildcard for BanEntry type
        int unbannedCount = 0;
        for (BanEntry<?> banEntry : banEntries) { // Iterate with wildcard
            // We used "LifestealPlugin" as the source when banning
            if (BAN_SOURCE.equalsIgnoreCase(banEntry.getSource())) {
                nameBanList.pardon(banEntry.getTarget()); // banEntry.getTarget() is the banned name (String)
                unbannedCount++;
                plugin.getLogger().info("Unbanned " + banEntry.getTarget() + " (was banned by " + banEntry.getSource() + ").");
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Unbanned " + unbannedCount + " player(s) that were banned by Lifesteal.");
        plugin.getLogger().info("Unbanned " + unbannedCount + " players with source " + BAN_SOURCE + ".");


        // 3. Note about offline players and persistence
        sender.sendMessage(ChatColor.GOLD + "Note: Health for offline players will be reset upon their next login once data persistence is fully implemented.");
        sender.sendMessage(ChatColor.AQUA + "Lifesteal reset process complete.");
        plugin.getLogger().info("Lifesteal reset complete. Offline player data will require persistence handling.");

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission(this.getPermission())) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            if ("CONFIRM".startsWith(args[0].toUpperCase())) {
                return Collections.singletonList("CONFIRM");
            }
            return Collections.singletonList("CONFIRM"); // Always suggest CONFIRM
        }
        return Collections.emptyList();
    }
}