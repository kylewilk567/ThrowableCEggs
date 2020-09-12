package xyz.ravencraft.ThrowableCEggs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class eggItem {
Main main = new Main();

public ItemStack getEgg() {
	
		ItemStack item = new ItemStack(Material.CREEPER_SPAWN_EGG, main.);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "Throwable Egg"));
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.translateAlternateColorCodes('&', "&aSpawns a creeper upon impact!"));
		meta.setLore(lore);
		
		meta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		return item;
	}
	
}
