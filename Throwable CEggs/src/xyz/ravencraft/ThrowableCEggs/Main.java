package xyz.ravencraft.ThrowableCEggs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin {
//private ConfigManager cfgm = new ConfigManager();
public Economy eco;
	
@Override
public void onEnable() {
	//Load config and eng ymls
//	loadConfigManager();
	loadEngManager();
	final ConfigManager cfgm = new ConfigManager();
	this.saveDefaultConfig(); //save config w/o getting rid of comments
	this.getServer().getPluginManager().registerEvents(new HitEvent(this), this); //import the event
	if(!setupEconomy()) {
		System.out.println(ChatColor.RED + "You must have Vault installed and an economy plugin");
		getServer().getPluginManager().disablePlugin(this);
		return;
	}
}

@Override
public void onDisable() {
}

//Setup Vault
private boolean setupEconomy() {
	RegisteredServiceProvider<Economy> economy = 
			getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	
	if(economy != null)
		eco = economy.getProvider();
	return (eco != null);
} 

/**public void loadConfigManager() {
	cfgm = new ConfigManager();
	cfgm.setupConfig();
	cfgm.saveConfig();
	cfgm.reloadConfig();
} **/

public void loadEngManager() {
	ConfigManager cfgm = new ConfigManager();
	cfgm.setupEng();
	cfgm.saveEng();
	cfgm.reloadEng();
	reloadConfig();
}

public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
	//Test
	YamlConfiguration languageConfig = ConfigManager.loadYamlFile(this, "eng");
	
	if(label.equalsIgnoreCase("cegg")) {
	
	if(args.length == 0) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lHelp message"));
	}
	
	if(args.length > 0) {
	//Cegg or Cegg help (console or player)
	if(args[0].equalsIgnoreCase("help")) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThrowable CEggs Help Menu\n"
				+ "&a/cegg help &2- displays this help menu.\n&a/cegg reload &2- reloads the plugin."));
	}
	//Cegg buy (player only) -------------------------------------------------------
	
	if(args[0].equalsIgnoreCase("buy")) {
	//Console error and end command
	if(!(sender instanceof Player)) {
		sender.sendMessage(languageConfig.getString("prefix") + "This command can only be sent by players!");
		return true;
	}
	//Perform inventory space check and argument check
	Player player = (Player) sender;
	if (player.getInventory().firstEmpty() == -1) {
		//inventory is FULL
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Inventory is full!"));
		return true;
	}
	try {
		Integer.parseInt(args[1]);
	} catch (Exception e){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Usage: /cegg buy <integer>"));
		return true;
	}
	//Cap purchase to a max of 64 eggs
	int amount = Integer.parseInt(args[1]);
	if(amount > 64) {
		player.sendMessage(languageConfig.getString("prefix") + ChatColor.RED + "You may only buy up to 64 eggs at once!");
		return true;
	}
	
	int cost = this.getConfig().getInt("Egg-cost.cost") * amount;
	//Check bypass cost permission
	if(player.hasPermission("cegg.bypass.cost")) {
		cost = 0;
	}
	this.eco.withdrawPlayer(player, cost);
	player.getInventory().addItem(getEgg(amount));
	player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("prefix")
			+ "&2You purchased &a" + amount + " &2throwable eggs for &a$" + cost + "&2!"));
	}
	//---------------------------------------------------------------------------------------------------------
	//Cegg reload----------------------------------------------------------------------------------------------
	if(args[0].equalsIgnoreCase("reload")) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("cegg.reload")) {
				loadEngManager();
				player.sendMessage(ChatColor.GREEN + "CEggs has been reloaded!");
			return true;
			}
			else {
				player.sendMessage(this.getConfig().getString("no-permission"));
				return true;
			}

		}
		sender.sendMessage(ChatColor.GREEN + "CEggs has been reloaded!");
		loadEngManager();
		return true;
//		loadConfigManager();

	}
	//-------------------------------------------------------------------------------------------------------
	//Cegg give (console or player) --------------------------------------------------------------------------
	if(args[0].equalsIgnoreCase("give")) {
		
		//Check permission	
		if(!(sender.hasPermission("Cegg.give"))) {

			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
					languageConfig.getString("no-permission")));
			return true;
		}
	
		//Check player argument and integer argument
		try {
		Bukkit.getPlayer(args[1]);
		Integer.parseInt(args[2]);
		} catch (Exception e) {
			sender.sendMessage("Usage: /Cegg give <player> <amount>");
			return true;
		}
		
		//Check inventory space
		Player player = Bukkit.getPlayer(args[1]);
		if (player.getInventory().firstEmpty() == -1) {
			sender.sendMessage("Player inventory full!");
			return true;
		}
	//Check permission	
	if(!(sender.hasPermission("Cegg.give"))) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("no-permission")));
		return true;
	}
	//Check cap of 64 eggs at once.
	final int amount = Integer.parseInt(args[2]); //final type means variable can't be modified
	if(amount > 64) {
		player.sendMessage(languageConfig.getString("prefix") + "You may only give up to 64 eggs at once!");
		return true;
	}
	//Checks done! Execute command
	player.getInventory().addItem(getEgg(amount));
	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("prefix") 
			+ "&2You have given &a" + args[2] + " &2throwable eggs to &a" + args[1]));
	player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("prefix") 
			+ "&2You have been given &a" + args[2] + " &2throwable eggs!"));
	}
	
	//----------------------------------------------------------------------------------------------------------------
	}	
	}
	return false;
}

public ItemStack getEgg(int amount) {
	
	ItemStack item = new ItemStack(Material.CREEPER_SPAWN_EGG, amount);
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "Throwable Egg"));
	List<String> lore = new ArrayList<String>();
	lore.add("");
	lore.add(ChatColor.translateAlternateColorCodes('&', "&aSpawns a normal creeper upon impact!")); //Not configurable
	meta.setLore(lore);
	
	meta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true);
	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	
	item.setItemMeta(meta);
	return item;
}
}
