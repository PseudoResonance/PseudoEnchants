package io.github.pseudoresonance.pseudoenchants.listeners;

import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import io.github.pseudoresonance.pseudoenchants.Config;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;

public class BlockBreakL implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		ItemStack is = p.getInventory().getItemInMainHand();
		if (is.getAmount() > 0) {
			if (is.hasItemMeta()) {
				ItemMeta im = is.getItemMeta();
				if (im.hasEnchant(PseudoEnchantment.EXCAVATION)) {
					if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
						if (Config.excavation) {
							RayTraceResult rtr = p.rayTraceBlocks(5, FluidCollisionMode.NEVER);
							BlockFace bf = rtr.getHitBlockFace();
							int broken = 0;
							if (bf == BlockFace.UP) {
								broken = breakUpDown(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block down = e.getBlock().getRelative(BlockFace.DOWN);
									if (breakBlock(down, is))
										broken++;
									broken += breakUpDown(down, is);
								}
							} else if (bf == BlockFace.DOWN) {
								broken = breakUpDown(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block up = e.getBlock().getRelative(BlockFace.UP);
									if (breakBlock(up, is))
										broken++;
									broken += breakUpDown(up, is);
								}
							} else if (bf == BlockFace.NORTH) {
								broken = breakNorthSouth(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block south = e.getBlock().getRelative(BlockFace.SOUTH);
									if (breakBlock(south, is))
										broken++;
									broken += breakNorthSouth(south, is);
								}
							} else if (bf == BlockFace.SOUTH) {
								broken = breakNorthSouth(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block north = e.getBlock().getRelative(BlockFace.NORTH);
									if (breakBlock(north, is))
										broken++;
									broken += breakNorthSouth(north, is);
								}
							} else if (bf == BlockFace.EAST) {
								broken = breakEastWest(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block west = e.getBlock().getRelative(BlockFace.WEST);
									if (breakBlock(west, is))
										broken++;
									broken += breakEastWest(west, is);
								}
							} else if (bf == BlockFace.WEST) {
								broken = breakEastWest(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block east = e.getBlock().getRelative(BlockFace.EAST);
									if (breakBlock(east, is))
										broken++;
									broken += breakEastWest(east, is);
								}
							}
							if (im instanceof Damageable && !im.isUnbreakable()) {
								int damage = 0;
								if (im.hasEnchant(Enchantment.DURABILITY)) {
									int level = im.getEnchantLevel(Enchantment.DURABILITY);
									double chance = 1.0 / (level + 1);
									for (int i = 0; i < broken; i++) {
										if (Math.random() <= chance)
											damage++;
									}
								} else {
									damage = broken;
								}
								Damageable damageable = (Damageable) im;
								damageable.setDamage(damageable.getDamage() + damage);
								is.setItemMeta((ItemMeta) damageable);
							}
						}
					}
				}
			}
		}
	}

	private static int breakUpDown(Block b, ItemStack is) {
		int broken = 0;
		if (breakBlock(b.getRelative(BlockFace.NORTH), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.NORTH_EAST), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.EAST), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH_EAST), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH_WEST), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.WEST), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.NORTH_WEST), is))
			broken++;
		return broken;
	}

	private static int breakNorthSouth(Block b, ItemStack is) {
		int broken = 0;
		Block up = b.getRelative(BlockFace.UP);
		if (breakBlock(up, is))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.EAST), is))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.WEST), is))
			broken++;
		Block down = b.getRelative(BlockFace.DOWN);
		if (breakBlock(down, is))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.EAST), is))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.WEST), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.EAST), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.WEST), is))
			broken++;
		return broken;
	}

	private static int breakEastWest(Block b, ItemStack is) {
		int broken = 0;
		Block up = b.getRelative(BlockFace.UP);
		if (breakBlock(up, is))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.NORTH), is))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.SOUTH), is))
			broken++;
		Block down = b.getRelative(BlockFace.DOWN);
		if (breakBlock(down, is))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.NORTH), is))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.SOUTH), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.NORTH), is))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH), is))
			broken++;
		return broken;
	}
	
	private static boolean breakBlock(Block b, ItemStack is) {
		if (!b.isEmpty() && !b.isLiquid() && b.getType().getHardness() > 0) {
			return b.breakNaturally(is);
		}
		return false;
	}

}
