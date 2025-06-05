package org.olavorw.lifesteal; // Make sure this matches your package!

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit; // Often useful, good to have the import
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;

import org.olavorw.lifesteal.commands.*;

public class Lifesteal extends JavaPlugin { // Class name matches 'main' in plugin.yml

    // Inside the Lifesteal class, but outside any methods
    public static final double DEFAULT_PLAYER_HEARTS = 10.0;
    public static final double MAX_POSSIBLE_HEARTS = 20.0; // 20 hearts = 40 HP
    public static final double MIN_POSSIBLE_HEARTS = 1.0;  // 1 heart = 2 HP
    public static final double HP_PER_HEART = 2.0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Lifesteal plugin by Olavorw has been enabled!");

        // We will register our PlayerDeathListener here
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getLogger().info("PlayerDeathListener registered.");

        registerHeartRecipe();

        // Register the HeartItemListener
        getServer().getPluginManager().registerEvents(new HeartItemListener(this), this);
        getLogger().info("HeartItemListener registered.");

        // Register the command executor for giving heart items
        boolean ghiRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new GiveHeartItemCommand(this));
        if (ghiRegistered) {
            getLogger().info("/giveheartitem command registered programmatically.");
        } else {
            getLogger().warning("Could not register /giveheartitem programmatically! Fallback prefix conflict?");
        }

        // Inside Lifesteal.java's onEnable() method
// ... (after registering GiveHeartItemCommand)

        boolean ghRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new GiveHeartCommand(this));
        if (ghRegistered) {
            getLogger().info("/giveheart command registered programmatically.");
        } else {
            getLogger().warning("Could not register /giveheart programmatically!");
        }

        // Inside Lifesteal.java's onEnable() method
// ... (after registering GiveHeartCommand)

        boolean rhRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new RemoveHeartCommand(this));
        if (rhRegistered) {
            getLogger().info("/removeheart command registered programmatically.");
        } else {
            getLogger().warning("Could not register /removeheart programmatically!");
        }

        // Inside Lifesteal.java's onEnable() method
// ... (after registering RemoveHeartCommand)

        boolean whRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new WithdrawHeartCommand(this));
        if (whRegistered) {
            getLogger().info("/withdrawheart command registered programmatically.");
        } else {
            getLogger().warning("Could not register /withdrawheart programmatically!");
        }

        // Inside Lifesteal.java's onEnable() method
// ... (after registering WithdrawHeartCommand or previous command)

        boolean reviveRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new RevivePlayerCommand(this));
        if (reviveRegistered) {
            getLogger().info("/reviveplayer command registered programmatically.");
        } else {
            getLogger().warning("Could not register /reviveplayer programmatically!");
        }

        // Inside Lifesteal.java's onEnable() method
// ... (after registering RevivePlayerCommand or previous command)

        boolean resetRegistered = Bukkit.getCommandMap().register(this.getDescription().getName().toLowerCase(), new LifestealResetCommand(this));
        if (resetRegistered) {
            getLogger().info("/lifestealreset command registered programmatically.");
        } else {
            getLogger().warning("Could not register /lifestealreset programmatically!");
        }



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Lifesteal plugin by Olavorw has been disabled!");
    }

    // Inside Lifesteal.java class
    private void registerHeartRecipe() {
        ItemStack heartResult = ItemManager.createHeartItem(); // Get the custom item

        // A NamespacedKey is a unique identifier for your recipe (plugin_name:recipe_key)
        NamespacedKey recipeKey = new NamespacedKey(this, "custom_heart");

        ShapedRecipe heartRecipe = new ShapedRecipe(recipeKey, heartResult);

        // Define the shape of the recipe
        heartRecipe.shape(
                "NDN",
                "GSG",
                "NDN"
        );

        // Set the ingredients for each character in the shape
        heartRecipe.setIngredient('N', Material.NETHERITE_INGOT);
        heartRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        heartRecipe.setIngredient('G', Material.GOLD_BLOCK);
        heartRecipe.setIngredient('S', Material.NETHER_STAR); // The ingredient Nether Star

        // Add the recipe to the server
        if (Bukkit.addRecipe(heartRecipe)) {
            getLogger().info("Custom Heart recipe registered successfully!");
        } else {
            getLogger().warning("Could not register Custom Heart recipe! Does a recipe with the key '" + recipeKey + "' already exist?");
        }
    }
}
