package io.github.pseudoresonance.pseudoenchants.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Excavation extends PseudoEnchantment {

	private static final String name = "excavation";
	private static final String friendlyName = "Excavation";

	public Excavation() {
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
		return 2;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
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
		if (check.contains("AXE") || check.contains("SHOVEL") || check.contains("ENCHANTED_BOOK"))
			return true;
		return false;
	}
	
	public int getAnvilMultiplier() {
		return 4;
	}
	
	public int getMinEnchantibility(int level) {
		return (level - 1) * 8 + 15;
	}
	
	public int getMaxEnchantibility(int level) {
		return getMinEnchantibility(level) + 25;
	}
	
	public int getEnchantmentWeight() {
		return 1;
	}

}
