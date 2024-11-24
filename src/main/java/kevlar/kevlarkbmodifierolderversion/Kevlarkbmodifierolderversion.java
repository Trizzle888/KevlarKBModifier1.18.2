// File: src/main/java/com/example/kevlarKBModifier/KevlarKBModifier.java

package kevlar.kevlarkbmodifierolderversion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Kevlarkbmodifierolderversion extends JavaPlugin implements Listener {

    private double arrowKnockbackMultiplier;
    private double snowballKnockbackMultiplier;
    private double eggKnockbackMultiplier;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();
        loadKnockbackValues();

        // Register event listener
        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("KevlarKBModifier has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("KevlarKBModifier has been disabled!");
    }

    private void loadKnockbackValues() {
        FileConfiguration config = getConfig();
        arrowKnockbackMultiplier = config.getDouble("knockback.arrow", 1.0);
        snowballKnockbackMultiplier = config.getDouble("knockback.snowball", 1.0);
        eggKnockbackMultiplier = config.getDouble("knockback.egg", 1.0);
    }

    @EventHandler
    public void onEntityHitByProjectile(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile)) {
            return;
        }

        Projectile projectile = (Projectile) event.getDamager();
        Entity hitEntity = event.getEntity();

        // Determine knockback multiplier
        double knockbackMultiplier = 1.0;

        if (projectile.getType().toString().equalsIgnoreCase("ARROW")) {
            knockbackMultiplier = arrowKnockbackMultiplier;
        } else if (projectile.getType().toString().equalsIgnoreCase("SNOWBALL")) {
            knockbackMultiplier = snowballKnockbackMultiplier;
        } else if (projectile.getType().toString().equalsIgnoreCase("EGG")) {
            knockbackMultiplier = eggKnockbackMultiplier;
        }

        // Apply custom knockback
        if (knockbackMultiplier != 1.0) {
            Vector knockback = hitEntity.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize();
            knockback.multiply(knockbackMultiplier);
            hitEntity.setVelocity(hitEntity.getVelocity().add(knockback));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kevlarkbreload")) {
            if (sender.hasPermission("kevlarkbmodifier.reload")) {
                reloadConfig();
                loadKnockbackValues();
                sender.sendMessage("§aKevlarKBModifier configuration reloaded!");
                getLogger().info("Configuration reloaded by " + sender.getName());
            } else {
                sender.sendMessage("§cYou do not have permission to reload the plugin!");
            }
            return true;
        }
        return false;
    }
}
