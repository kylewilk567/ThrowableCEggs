package xyz.ravencraft.ThrowableCEggs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HitEvent implements Listener {

	private Main plugin;
	public HitEvent(Main plugin) {
		this.plugin = plugin;

	}
	private ConfigManager cfgm;
	public HitEvent(ConfigManager variable) {
		this.cfgm = variable;
		YamlConfiguration languageConfig = ConfigManager.loadYamlFile(this.plugin, "eng");
	}

	
	

	@EventHandler
	public void onLand(ProjectileHitEvent event) {
		if(event.getEntityType() == EntityType.SNOWBALL) { //How to make it a spawn egg?
			if(event.getEntity().getShooter() instanceof Player) {
				Player player = (Player) event.getEntity().getShooter();
				Location loc = event.getEntity().getLocation();
			//Change location to be next to block, towards player, not spawn in air.
				
				//TEMP
				loc.setY(loc.getY() + 1);
				loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
				
				
			}
			
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("cegg.use")) {
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if(event.getItem() == null) return;
				ItemStack item = event.getItem();
	//Check if the item they are holding is a REGULAR creeper egg
				if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.CREEPER_SPAWN_EGG)) {
					if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore()
							.toString().contains("normal")) {
						event.getPlayer().launchProjectile(Snowball.class);
						if(item.getAmount() > 1) {
							item.setAmount(item.getAmount() - 1);
						}
						else {
							event.getPlayer().getInventory().remove(item);
						}
					}
				}
			}
	}
		if(!(player.hasPermission("cegg.use"))){
			YamlConfiguration languageConfig = ConfigManager.loadYamlFile(this.plugin, "eng");
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("no-permission")));
		}
	}	
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		if(event.getEntityType() == EntityType.CREEPER && event.getSpawnReason() == SpawnReason.CUSTOM) {
			event.getEntity().setGravity(this.plugin.getConfig().getBoolean("Gravity.enabled"));
			event.getEntity().setAI(false);
			Creeper creeper = (Creeper) event.getEntity(); //How to set explode radius and fuse???
			creeper.setMaxFuseTicks(this.plugin.getConfig().getInt("fuse-ticks"));
			creeper.setExplosionRadius(this.plugin.getConfig().getInt("explosion-radius"));
			creeper.ignite();
			}
		}
	}
	
