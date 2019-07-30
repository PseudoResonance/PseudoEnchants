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

import io.github.pseudoresonance.pseudoenchants.PseudoEnchants;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;

public class EnchantL implements Listener {
	
	private static HashMap<String, Integer> costMap = new HashMap<String, Integer>();
	private static HashMap<String, Block> anvilMap = new HashMap<String, Block>();

	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		for (Entry<Enchantment, Integer> enchSet : e.getEnchantsToAdd().entrySet()) {
			if (enchSet.getKey() instanceof PseudoEnchantment) {
				PseudoEnchantment.addLoreEnchantment(e.getItem(), (PseudoEnchantment) enchSet.getKey(), enchSet.getValue());
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
									b.setType(Material.AIR);
									b.getWorld().playSound(b.getLocation(), Sound.BLOCK_ANVIL_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
									anvilMap.remove(e.getWhoClicked().getName());
									e.getView().close();
								}
							}
						}
					}
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
					aInv.clear();
					PseudoEnchants.message.sendPluginMessage(e.getWhoClicked(), "Click: Cost: " + cost + " Result: " + result.getType());
					return;
				}
				aInv.setItem(2, result);
				aInv.setRepairCost(0);
				e.getView().setProperty(Property.REPAIR_COST, 0);
				e.setResult(Result.DENY);
				PseudoEnchants.message.sendPluginMessage(e.getWhoClicked(), "Click: Cost: None Result: None");
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		if (b.getType() == Material.ANVIL || b.getType() == Material.CHIPPED_ANVIL || b.getType() == Material.DAMAGED_ANVIL) {
			if (e.useInteractedBlock() != Result.DENY) {
				anvilMap.put(e.getPlayer().getName(), b);
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
				PseudoEnchants.message.sendPluginMessage(e.getViewers().get(0), "Anvil: Cost: " + cost + " Result: " + result.getType());
				return;
			}
			costMap.remove(e.getViewers().get(0).getName());
			e.setResult(result);
			inv.setRepairCost(0);
			e.getView().setProperty(Property.REPAIR_COST, 0);
			PseudoEnchants.message.sendPluginMessage(e.getViewers().get(0), "Anvil: Cost: None Result: None");
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
