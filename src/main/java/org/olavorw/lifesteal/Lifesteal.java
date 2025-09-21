package org.olavorw.lifesteal; 

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit; 
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;

import org.olavorw.lifesteal.commands.*;

public class Lifesteal extends JavaPlugin { 

    
    public static final double DEFAULT_PLAYER_HEARTS = 10.0;
    public static final double MAX_POSSIBLE_HEARTS = 20.0; 
    public static final double MIN_POSSIBLE_HEARTS = 0.0;  
    public static final double HP_PER_HEART = 2.0;

    @Override
    public void onEnable() {
        
        getLogger().info("Lifesteal plugin by Olavorw has been enabled!");

        
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getLogger().info("PlayerDeathListener registered.");

        registerHeartRecipe();

        
        getServer().getPluginManager().registerEvents(new HeartItemListener(this), this);
        getLogger().info("HeartItemListener registered.");

        
        boolean ghiRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new GiveHeartItemCommand(this));
        if (ghiRegistered) {
            getLogger().info("/giveheartitem command registered programmatically.");
        } else {
            getLogger().warning("Could not register /giveheartitem programmatically! Fallback prefix conflict?");
        }

        


        boolean ghRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new GiveHeartCommand(this));
        if (ghRegistered) {
            getLogger().info("/giveheart command registered programmatically.");
        } else {
            getLogger().warning("Could not register /giveheart programmatically!");
        }

        


        boolean rhRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new RemoveHeartCommand(this));
        if (rhRegistered) {
            getLogger().info("/removeheart command registered programmatically.");
        } else {
            getLogger().warning("Could not register /removeheart programmatically!");
        }

        


        boolean whRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new WithdrawHeartCommand(this));
        if (whRegistered) {
            getLogger().info("/withdrawheart command registered programmatically.");
        } else {
            getLogger().warning("Could not register /withdrawheart programmatically!");
        }

        


        boolean reviveRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new RevivePlayerCommand(this));
        if (reviveRegistered) {
            getLogger().info("/reviveplayer command registered programmatically.");
        } else {
            getLogger().warning("Could not register /reviveplayer programmatically!");
        }

        


        boolean resetRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new LifestealResetCommand(this));
        if (resetRegistered) {
            getLogger().info("/lifestealreset command registered programmatically.");
        } else {
            getLogger().warning("Could not register /lifestealreset programmatically!");
        }

        

        boolean resetPlayerHeartsRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new ResetPlayerHeartsCommand(this));
        if (resetPlayerHeartsRegistered) {
            getLogger().info("/resethearts command registered programmatically.");
        } else {
            getLogger().warning("Could not register /resethearts programmatically!");
        }

        boolean checkHealthRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new CheckHealthCommand(this));
        if (checkHealthRegistered) {
            getLogger().info("/checkhealth command registered programmatically.");
        } else {
            getLogger().warning("Could not register /checkhealth programmatically!");
        }



    }

    @Override
    public void onDisable() {
        
        getLogger().info("Lifesteal plugin by Olavorw has been disabled!");
    }

    
    private void registerHeartRecipe() {
        ItemStack heartResult = ItemManager.createHeartItem(); 

        
        NamespacedKey recipeKey = new NamespacedKey(this, "custom_heart");

        ShapedRecipe heartRecipe = new ShapedRecipe(recipeKey, heartResult);

        
        heartRecipe.shape(
                "NDN",
                "GSG",
                "NDN"
        );

        
        heartRecipe.setIngredient('N', Material.NETHERITE_INGOT);
        heartRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        heartRecipe.setIngredient('G', Material.GOLD_BLOCK);
        heartRecipe.setIngredient('S', Material.NETHER_STAR); 

        
        if (Bukkit.addRecipe(heartRecipe)) {
            getLogger().info("Custom Heart recipe registered successfully!");
        } else {
            getLogger().warning("Could not register Custom Heart recipe! Does a recipe with the key '" + recipeKey + "' already exist?");
        }
    }
}
