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
	public void onTeleport(BlockBreakEvent e) {
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
									if (down.breakNaturally(is))
										broken++;
									broken += breakUpDown(down, is);
								}
							} else if (bf == BlockFace.DOWN) {
								broken = breakUpDown(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block up = e.getBlock().getRelative(BlockFace.UP);
									if (up.breakNaturally(is))
										broken++;
									broken += breakUpDown(up, is);
								}
							} else if (bf == BlockFace.NORTH) {
								broken = breakNorthSouth(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block south = e.getBlock().getRelative(BlockFace.SOUTH);
									if (south.breakNaturally(is))
										broken++;
									broken += breakNorthSouth(south, is);
								}
							} else if (bf == BlockFace.SOUTH) {
								broken = breakNorthSouth(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block north = e.getBlock().getRelative(BlockFace.NORTH);
									if (north.breakNaturally(is))
										broken++;
									broken += breakNorthSouth(north, is);
								}
							} else if (bf == BlockFace.EAST) {
								broken = breakEastWest(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block west = e.getBlock().getRelative(BlockFace.WEST);
									if (west.breakNaturally(is))
										broken++;
									broken += breakEastWest(west, is);
								}
							} else if (bf == BlockFace.WEST) {
								broken = breakEastWest(e.getBlock(), is);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block east = e.getBlock().getRelative(BlockFace.EAST);
									if (east.breakNaturally(is))
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
		if (b.getRelative(BlockFace.NORTH).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.NORTH_EAST).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.EAST).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.SOUTH_EAST).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.SOUTH).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.SOUTH_WEST).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.WEST).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.NORTH_WEST).breakNaturally(is))
			broken++;
		return broken;
	}

	private static int breakNorthSouth(Block b, ItemStack is) {
		int broken = 0;
		Block up = b.getRelative(BlockFace.UP);
		if (up.breakNaturally(is))
			broken++;
		if (up.getRelative(BlockFace.EAST).breakNaturally(is))
			broken++;
		if (up.getRelative(BlockFace.WEST).breakNaturally(is))
			broken++;
		Block down = b.getRelative(BlockFace.DOWN);
		if (down.breakNaturally(is))
			broken++;
		if (down.getRelative(BlockFace.EAST).breakNaturally(is))
			broken++;
		if (down.getRelative(BlockFace.WEST).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.EAST).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.WEST).breakNaturally(is))
			broken++;
		return broken;
	}

	private static int breakEastWest(Block b, ItemStack is) {
		int broken = 0;
		Block up = b.getRelative(BlockFace.UP);
		if (up.breakNaturally(is))
			broken++;
		if (up.getRelative(BlockFace.NORTH).breakNaturally(is))
			broken++;
		if (up.getRelative(BlockFace.SOUTH).breakNaturally(is))
			broken++;
		Block down = b.getRelative(BlockFace.DOWN);
		if (down.breakNaturally(is))
			broken++;
		if (down.getRelative(BlockFace.NORTH).breakNaturally(is))
			broken++;
		if (down.getRelative(BlockFace.SOUTH).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.NORTH).breakNaturally(is))
			broken++;
		if (b.getRelative(BlockFace.SOUTH).breakNaturally(is))
			broken++;
		return broken;
	}

}
