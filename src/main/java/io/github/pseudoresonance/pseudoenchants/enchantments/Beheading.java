package io.github.pseudoresonance.pseudoenchants.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Beheading extends PseudoEnchantment {

	private static final String name = "beheading";
	private static final String friendlyName = "Beheading";

	public Beheading() {
		super(name);
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}

	@Override
	public String getName() {
		return name.toUpperCase();
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		String check = item.getType().toString();
		if ((check.contains("AXE") && !check.contains("PICKAXE")) || check.contains("SWORD") || check.contains("ENCHANTED_BOOK"))
			return true;
		return false;
	}
	
	public int getAnvilMultiplier() {
		return 1;
	}
	
	public int getMinEnchantibility(int level) {
		return (level - 1) * 4 + 5;
	}
	
	public int getMaxEnchantibility(int level) {
		return getMinEnchantibility(level) + 15;
	}
	
	public int getEnchantmentWeight() {
		return 1;
	}

}
