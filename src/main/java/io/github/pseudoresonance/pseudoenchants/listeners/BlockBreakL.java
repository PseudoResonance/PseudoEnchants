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

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.github.pseudoresonance.pseudoenchants.Config;
import io.github.pseudoresonance.pseudoenchants.PseudoEnchants;
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
								broken = breakUpDown(e.getBlock(), is, p);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block down = e.getBlock().getRelative(BlockFace.DOWN);
									if (breakBlock(down, is, p))
										broken++;
									broken += breakUpDown(down, is, p);
								}
							} else if (bf == BlockFace.DOWN) {
								broken = breakUpDown(e.getBlock(), is, p);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block up = e.getBlock().getRelative(BlockFace.UP);
									if (breakBlock(up, is, p))
										broken++;
									broken += breakUpDown(up, is, p);
								}
							} else if (bf == BlockFace.NORTH) {
								broken = breakNorthSouth(e.getBlock(), is, p);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block south = e.getBlock().getRelative(BlockFace.SOUTH);
									if (breakBlock(south, is, p))
										broken++;
									broken += breakNorthSouth(south, is, p);
								}
							} else if (bf == BlockFace.SOUTH) {
								broken = breakNorthSouth(e.getBlock(), is, p);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block north = e.getBlock().getRelative(BlockFace.NORTH);
									if (breakBlock(north, is, p))
										broken++;
									broken += breakNorthSouth(north, is, p);
								}
							} else if (bf == BlockFace.EAST) {
								broken = breakEastWest(e.getBlock(), is, p);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block west = e.getBlock().getRelative(BlockFace.WEST);
									if (breakBlock(west, is, p))
										broken++;
									broken += breakEastWest(west, is, p);
								}
							} else if (bf == BlockFace.WEST) {
								broken = breakEastWest(e.getBlock(), is, p);
								if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
									Block east = e.getBlock().getRelative(BlockFace.EAST);
									if (breakBlock(east, is, p))
										broken++;
									broken += breakEastWest(east, is, p);
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

	private static int breakUpDown(Block b, ItemStack is, Player p) {
		int broken = 0;
		if (breakBlock(b.getRelative(BlockFace.NORTH), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.NORTH_EAST), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.EAST), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH_EAST), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH_WEST), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.WEST), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.NORTH_WEST), is, p))
			broken++;
		return broken;
	}

	private static int breakNorthSouth(Block b, ItemStack is, Player p) {
		int broken = 0;
		Block up = b.getRelative(BlockFace.UP);
		if (breakBlock(up, is, p))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.EAST), is, p))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.WEST), is, p))
			broken++;
		Block down = b.getRelative(BlockFace.DOWN);
		if (breakBlock(down, is, p))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.EAST), is, p))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.WEST), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.EAST), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.WEST), is, p))
			broken++;
		return broken;
	}

	private static int breakEastWest(Block b, ItemStack is, Player p) {
		int broken = 0;
		Block up = b.getRelative(BlockFace.UP);
		if (breakBlock(up, is, p))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.NORTH), is, p))
			broken++;
		if (breakBlock(up.getRelative(BlockFace.SOUTH), is, p))
			broken++;
		Block down = b.getRelative(BlockFace.DOWN);
		if (breakBlock(down, is, p))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.NORTH), is, p))
			broken++;
		if (breakBlock(down.getRelative(BlockFace.SOUTH), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.NORTH), is, p))
			broken++;
		if (breakBlock(b.getRelative(BlockFace.SOUTH), is, p))
			broken++;
		return broken;
	}
	
	private static boolean breakBlock(Block b, ItemStack is, Player p) {
		if (!b.isEmpty() && !b.isLiquid() && b.getType().getHardness() > 0) {
			if (PseudoEnchants.isWorldGuardLoaded) {
				LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(p);
				Location loc = new Location(lp.getWorld(), b.getX(), b.getY(), b.getZ());
				RegionContainer cont = WorldGuard.getInstance().getPlatform().getRegionContainer();
				RegionQuery query = cont.createQuery();
				if (!WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(lp, lp.getWorld()) && !query.testBuild(loc, lp)) {
					return false;
				}
			}
			return b.breakNaturally(is);
		}
		return false;
	}

}
