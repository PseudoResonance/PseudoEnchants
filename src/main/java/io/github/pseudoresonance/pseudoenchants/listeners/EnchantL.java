package io.github.pseudoresonance.pseudoenchants.listeners;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;

public class EnchantL implements Listener {
	
	private static HashMap<String, Integer> costMap = new HashMap<String, Integer>();
	private static HashMap<String, Block> anvilMap = new HashMap<String, Block>();

	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		ItemStack is = e.getItem();
		int cost = e.getExpLevelCost();
		is = PseudoEnchantment.stripLoreEnchantments(is);
		int added = e.getEnchantsToAdd().size();
		double mult = Math.pow(0.75, added);
		double randa = Math.random();
		if (randa <= (((cost * Math.pow(0.8, added)) + 1) / 37.0)) {
			boolean firstRun = true;
			HashMap<PseudoEnchantment, Integer> possible = new HashMap<PseudoEnchantment, Integer>();
			for (PseudoEnchantment ench : PseudoEnchantment.getEnchantments()) {
				if (ench.canEnchantItem(is) && !ench.isTreasure()) {
					for (int i = 1; i <= ench.getMaxLevel(); i++) {
						int min = ench.getMinEnchantibility(i);
						if (cost >= min) {
							int max = ench.getMaxEnchantibility(i);
							if (cost <= max) {
								possible.put(ench, i);
							}
						}
					}
				}
			}
			cost *= mult;
			if (possible.isEmpty())
				return;
			while (true) {
				if (firstRun || Math.random() <= ((cost + 1) / 50.0)) {
					if (possible.isEmpty())
						return;
					if (firstRun)
						firstRun = false;
					int maxWeight = 0;
					for (PseudoEnchantment ench : possible.keySet()) {
						maxWeight += ench.getEnchantmentWeight();
					}
					double rand = (Math.random() * (maxWeight / 2.0));
					for (PseudoEnchantment ench : possible.keySet()) {
						rand -= ench.getEnchantmentWeight();
						if (rand <= 0) {
							is.addEnchantment(ench, possible.get(ench));
							is = PseudoEnchantment.addLoreEnchantment(is, ench, possible.get(ench));
							possible.remove(ench);
						}
					}
				} else {
					return;
				}
				cost /= 2;
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getType() == InventoryType.ANVIL) {
			if (e.getRawSlot() == 2) {
				AnvilInventory aInv = (AnvilInventory) inv;
				ItemStack left = aInv.getItem(0);
				ItemStack right = aInv.getItem(1);
				ItemStack result = aInv.getItem(2);
				SimpleEntry<ItemStack, Boolean> ret = getResultItem(left, right, result);
				if (!ret.getValue())
					return;
				result = ret.getKey();
				if (result != null && result.getAmount() > 0) {
					if (result.hasItemMeta()) {
						if (aInv.getRenameText() != null && !aInv.getRenameText().equals("")) {
							ItemMeta im = result.getItemMeta();
							im.setDisplayName(aInv.getRenameText());
							result.setItemMeta(im);
						}
					}
					int cost = aInv.getRepairCost();
					for (Entry<Enchantment, Integer> ench : result.getEnchantments().entrySet()) {
						if (ench.getKey() instanceof PseudoEnchantment) {
							if (!left.containsEnchantment(ench.getKey()) || left.getEnchantmentLevel(ench.getKey()) != ench.getValue()) {
								cost += ench.getValue() * ((PseudoEnchantment) ench.getKey()).getAnvilMultiplier();
							}
						}
					}
					if (costMap.containsKey(e.getWhoClicked().getName())) {
						cost = costMap.get(e.getWhoClicked().getName());
					}
					e.getView().setCursor(result);
					Player p = (Player) e.getWhoClicked();
					if (p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
						p.setLevel(p.getLevel() - cost);
						if (Math.random() <= 0.12) {
							if (anvilMap.containsKey(e.getWhoClicked().getName())) {
								Block b = anvilMap.get(e.getWhoClicked().getName());
								if (b.getType() == Material.ANVIL) {
									b.setType(Material.CHIPPED_ANVIL);
								} else if (b.getType() == Material.CHIPPED_ANVIL) {
									b.setType(Material.DAMAGED_ANVIL);
								} else if (b.getType() == Material.DAMAGED_ANVIL) {
									aInv.clear();
									b.setType(Material.AIR);
									b.getWorld().playSound(b.getLocation(), Sound.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1.0f, 1.0f);
									anvilMap.remove(e.getWhoClicked().getName());
									e.getView().close();
									return;
								}
							}
						}
					}
					aInv.clear();
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
					return;
				}
				aInv.setItem(2, result);
				aInv.setRepairCost(0);
				e.getView().setProperty(Property.REPAIR_COST, 0);
				e.setResult(Result.DENY);
			}
		} else if (inv.getType() == InventoryType.GRINDSTONE) {
			ItemStack is = inv.getItem(2);
			if (is != null & is.getAmount() > 0) {
				is = PseudoEnchantment.stripLoreEnchantments(is);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		if (b != null) {
			if (b.getType() == Material.ANVIL || b.getType() == Material.CHIPPED_ANVIL || b.getType() == Material.DAMAGED_ANVIL) {
				if (e.useInteractedBlock() != Result.DENY) {
					anvilMap.put(e.getPlayer().getName(), b);
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getInventory().getType() == InventoryType.ANVIL) {
			costMap.remove(e.getPlayer().getName());
			anvilMap.remove(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onAnvil(PrepareAnvilEvent e) {
		AnvilInventory inv = e.getInventory();
		if (inv != null) {
			ItemStack left = inv.getItem(0);
			ItemStack right = inv.getItem(1);
			ItemStack result = e.getResult();
			SimpleEntry<ItemStack, Boolean> ret = getResultItem(left, right, result);
			if (!ret.getValue())
				return;
			result = ret.getKey();
			if (result != null && result.getAmount() > 0) {
				if (result.hasItemMeta()) {
					if (inv.getRenameText() != null && !inv.getRenameText().equals("")) {
						ItemMeta im = result.getItemMeta();
						im.setDisplayName(inv.getRenameText());
						result.setItemMeta(im);
					}
				}
				int cost = inv.getRepairCost();
				for (Entry<Enchantment, Integer> ench : result.getEnchantments().entrySet()) {
					if (ench.getKey() instanceof PseudoEnchantment) {
						if (!left.containsEnchantment(ench.getKey()) || left.getEnchantmentLevel(ench.getKey()) != ench.getValue()) {
							cost += ench.getValue() * ((PseudoEnchantment) ench.getKey()).getAnvilMultiplier();
						}
					}
				}
				costMap.put(e.getViewers().get(0).getName(), cost);
				e.setResult(result);
				inv.setRepairCost(cost);
				e.getView().setProperty(Property.REPAIR_COST, cost);
				return;
			}
			costMap.remove(e.getViewers().get(0).getName());
			e.setResult(result);
			inv.setRepairCost(0);
			e.getView().setProperty(Property.REPAIR_COST, 0);
		}
	}
	
	public static SimpleEntry<ItemStack, Boolean> getResultItem(ItemStack left, ItemStack right, ItemStack result) {
		boolean hasPseudoEnchant = false;
		try {
			if (left == null || left.getAmount() == 0) {
				return new SimpleEntry<ItemStack, Boolean>(null, hasPseudoEnchant);
			}
			if (right == null || right.getAmount() == 0) {
				result = left.clone();
				result = PseudoEnchantment.stripLoreEnchantments(result);
				for (Entry<Enchantment, Integer> ench : left.getEnchantments().entrySet()) {
					if (ench.getKey() instanceof PseudoEnchantment) {
						hasPseudoEnchant = true;
						int level = ench.getValue();
						result = PseudoEnchantment.addLoreEnchantment(result, (PseudoEnchantment) ench.getKey(), level);
					}
				}
				return new SimpleEntry<ItemStack, Boolean>(result, hasPseudoEnchant);
			}
			if (left != null && left.getAmount() != 0 && right != null && right.getAmount() != 0) {
				if (left.getType() == right.getType() || right.getType() == Material.ENCHANTED_BOOK) {
					if (result == null || result.getAmount() == 0) {
						result = left.clone();
					}
					result = PseudoEnchantment.stripLoreEnchantments(result);
					for (Entry<Enchantment, Integer> ench : left.getEnchantments().entrySet()) {
						if (ench.getKey() instanceof PseudoEnchantment) {
							hasPseudoEnchant = true;
							PseudoEnchantment enchantment = (PseudoEnchantment) ench.getKey();
							int level = ench.getValue();
							if (right.containsEnchantment(enchantment)) {
								int secondLevel = right.getEnchantmentLevel(enchantment);
								if (level == secondLevel && level < enchantment.getMaxLevel()) {
									level++;
								} else if (secondLevel > level) {
									level = secondLevel;
								}
							}
							result.addEnchantment(enchantment, level);
							result = PseudoEnchantment.addLoreEnchantment(result, enchantment, level);
						}
					}
					for (Entry<Enchantment, Integer> ench : right.getEnchantments().entrySet()) {
						if (ench.getKey() instanceof PseudoEnchantment && !result.containsEnchantment(ench.getKey())) {
							hasPseudoEnchant = true;
							PseudoEnchantment enchantment = (PseudoEnchantment) ench.getKey();
							if (enchantment.canEnchantItem(result)) {
								int level = ench.getValue();
								result.addEnchantment(enchantment, level);
								result = PseudoEnchantment.addLoreEnchantment(result, enchantment, level);
							}
						}
					}
					return new SimpleEntry<ItemStack, Boolean>(result, hasPseudoEnchant);
				}
			}
		} catch (Exception e) {}
		return new SimpleEntry<ItemStack, Boolean>(null, hasPseudoEnchant);
	}

}
