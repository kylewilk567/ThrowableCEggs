package xyz.ravencraft.ThrowableCEggs;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

	private Main plugin = Main.getPlugin(Main.class);
	
	//Config files declared here
	public FileConfiguration configuration;
	public File configFile;
	//---------------------------------
	
	/**
	//Setup directory folder HERE-----------------------------------------
	public void setupConfig() {
		
		//Create datafolder if folder does not exist
		if(!plugin.getDataFolder().exists()) {
		plugin.getDataFolder().mkdir();	
		}
		//-------------------------------------------------------------------
		
		//Config.yml functions HERE-----------------------------------
		configFile = new File(plugin.getDataFolder(), "config.yml");
		
		
		//Create file if it exists
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "config.yml has been created.");
				
			}
			catch(IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create config.yml!");
			}
		}
		
		configuration = YamlConfiguration.loadConfiguration(configFile);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "config.yml has been loaded.");
	}
	
	
	public FileConfiguration getConfig() {
		return configuration;
	}
	
	public void saveConfig() {
		try {
			configuration.save(configFile);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "config.yml has been saved.");
		} catch(IOException e ) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save config.yml!");
		}
	}
	
	public void reloadConfig() {
		configuration = YamlConfiguration.loadConfiguration(configFile);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "config.yml has been reloaded.");
	}
	**/
	//---------------------------------------------------------------------------------------------------------------
	
	//eng.yml File HERE
	public FileConfiguration engConfig;
	public File engFile;
	


	public void setupEng() {
		
	engFile = new File(plugin.getDataFolder(), "eng.yml");
	
	//Create file if it exists
	if(!engFile.exists()) {
			plugin.saveResource("eng.yml", false);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "eng.yml has been created.");
			
		//catch(IOException e) {
		//	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create eng.yml!");
	//	}
	}
	engConfig = YamlConfiguration.loadConfiguration(engFile);
	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "eng.yml has been loaded.");
}


public FileConfiguration getEng() {
	return engConfig;
}

public void saveEng() {
	try {
		engConfig.save(engFile);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "eng.yml has been saved.");
	} catch(IOException e ) {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save eng.yml!");
	}
}

public void reloadEng() {
	engConfig = YamlConfiguration.loadConfiguration(engFile);
	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "config.yml has been reloaded.");
}
//Broader loading function
@Nullable
public static YamlConfiguration loadYamlFile(Plugin plugin, String fileName) {
    File f = new File(plugin.getDataFolder().getPath() + File.separator + fileName + ".yml");
    if (!f.exists()) {
        return null;
    }
    return YamlConfiguration.loadConfiguration(f);
}

	
}
