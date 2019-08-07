package io.github.pseudoresonance.pseudoenchants.enchantments;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoenchants.Config;
import io.github.pseudoresonance.pseudoenchants.PseudoEnchants;

public abstract class PseudoEnchantment extends Enchantment {

	public static final PseudoEnchantment EXCAVATION = new Excavation();
	public static final PseudoEnchantment BEHEADING = new Beheading();

	private static final String[] numerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
	private static ArrayList<PseudoEnchantment> enchantments = new ArrayList<PseudoEnchantment>();
	
	public static final String colorPrefix = "§7";
	
	public abstract String getFriendlyName();
	
	public PseudoEnchantment(String name) {
		super(new NamespacedKey(PseudoEnchants.plugin, name));
	}
	
	public abstract int getAnvilMultiplier();
	
	public abstract int getMinEnchantibility(int level);
	
	public abstract int getMaxEnchantibility(int level);
	
	public abstract int getEnchantmentWeight();
	
	public static boolean registerEnchantments() {
		boolean success = true;
		if (Config.excavation) success = (registerEnchantment(EXCAVATION) == false) ? false : success;
		if (Config.beheading) success = (registerEnchantment(BEHEADING) == false) ? false : success;
		return success;
	}
	
	private static boolean registerEnchantment(PseudoEnchantment ench) {
		try {
			PseudoEnchants.message.sendConsolePluginMessage("Registering " + ench.getKey());
			Enchantment.registerEnchantment(ench);
			enchantments.add(ench);
		} catch (IllegalStateException | IllegalArgumentException e) {
			PseudoEnchants.message.sendConsolePluginError(Errors.CUSTOM, "Could not register " + ench.getKey() + "!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static ArrayList<PseudoEnchantment> getEnchantments() {
		return enchantments;
	}
	
	public static ItemStack addLoreEnchantment(ItemStack is, PseudoEnchantment ench, int level) {
		if (is.hasItemMeta()) {
			ItemMeta im = is.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(colorPrefix + getEnchantmentString(ench, level));
			if (im.hasLore())
				lore.addAll(im.getLore());
			im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}
	
	public static ItemStack stripLoreEnchantment(ItemStack is, PseudoEnchantment ench) {
		if (is.hasItemMeta()) {
			ItemMeta im = is.getItemMeta();
			if (im.hasLore()) {
				List<String> lore = im.getLore();
				for (int i = lore.size() - 1; i >= 0; i--) {
					String s = lore.get(i);
					if (s.startsWith(colorPrefix)) {
						String strip = s.substring(2);
						if (strip.startsWith(ench.getFriendlyName())) {
							lore.remove(i);
							break;
						}
					}
				}
				im.setLore(lore);
				is.setItemMeta(im);
			}
		}
		return is;
	}
	
	public static ItemStack stripLoreEnchantments(ItemStack is) {
		if (is.hasItemMeta()) {
			ItemMeta im = is.getItemMeta();
			if (im.hasLore()) {
				List<String> lore = im.getLore();
				for (int i = lore.size() - 1; i >= 0; i--) {
					String s = lore.get(i);
					if (s.startsWith(colorPrefix)) {
						String strip = s.substring(2);
						for (PseudoEnchantment ench : enchantments) {
							if (strip.startsWith(ench.getFriendlyName())) {
								lore.remove(i);
								break;
							}
						}
					}
				}
				im.setLore(lore);
				is.setItemMeta(im);
			}
		}
		return is;
	}
	
	public static String getEnchantmentString(PseudoEnchantment ench, int level) {
		if (ench.getMaxLevel() == 1 && level == 1)
			return ench.getFriendlyName();
		else
			return ench.getFriendlyName() + " " + ((level > 0 && level <= 10) ? numerals[level - 1] : level);
	}

}
