package org.olavorw.lifesteal.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand; 
import org.olavorw.lifesteal.Lifesteal;         
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CommandBase extends BukkitCommand {
    protected final Lifesteal plugin;

    protected CommandBase(Lifesteal plugin, @NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases, String permission) {
        super(name); 
        this.plugin = plugin;
        this.description = description;         
        this.usageMessage = usageMessage;       
        this.setAliases(aliases);               

        if (permission != null && !permission.isEmpty()) {
            this.setPermission(permission);     
            this.setPermissionMessage(ChatColor.RED + "You do not have permission to use this command."); 
        }
    }

    /**
     * This method is called when the command is executed by a sender
     * after permission checks have passed.
     *
     * @param sender       Source of the command
     * @param commandLabel Alias of the command which was used
     * @param args         Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public abstract boolean onValidatedCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args);

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!plugin.isEnabled()) {
            sender.sendMessage(ChatColor.RED + "This plugin is disabled.");
            return true; 
        }

        
        
        if (!testPermission(sender)) {
            return true; 
        }

        
        return onValidatedCommand(sender, commandLabel, args);
    }

    
    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args); 
    }
}