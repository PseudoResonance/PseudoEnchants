package io.github.pseudoresonance.pseudoenchants.listeners;

import java.util.UUID;

import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.github.pseudoresonance.pseudoapi.bukkit.utils.HeadUtils;
import io.github.pseudoresonance.pseudoenchants.Config;
import io.github.pseudoresonance.pseudoenchants.PseudoEnchants;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;

public class BlockL implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		ItemStack is = e.getItemInHand();
		Block b = e.getBlock();
		if (is.getType() == Material.PLAYER_HEAD) {
			ItemMeta im = is.getItemMeta();
			String name = im.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "HeadName"), PersistentDataType.STRING);
			if (name != null) {
				String uuid = im.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "Uuid"), PersistentDataType.STRING);
				if (uuid == null) {
					String base64 = im.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "Base64"), PersistentDataType.STRING);
					if (base64 != null) {
						Skull skull = (Skull) b.getState();
						skull.getPersistentDataContainer().set(new NamespacedKey(PseudoEnchants.plugin, "HeadName"), PersistentDataType.STRING, name);
						skull.getPersistentDataContainer().set(new NamespacedKey(PseudoEnchants.plugin, "Base64"), PersistentDataType.STRING, base64);
						skull.update();
					}
				} else {
					Skull skull = (Skull) b.getState();
					skull.getPersistentDataContainer().set(new NamespacedKey(PseudoEnchants.plugin, "HeadName"), PersistentDataType.STRING, name);
					skull.getPersistentDataContainer().set(new NamespacedKey(PseudoEnchants.plugin, "Uuid"), PersistentDataType.STRING, uuid);
					skull.update();
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		Player p = e.getPlayer();
		ItemStack is = p.getInventory().getItemInMainHand();
		if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
			if (b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD) {
				Skull skull = (Skull) b.getState();
				String name = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "HeadName"), PersistentDataType.STRING);
				if (name != null) {
					ItemStack drop = null;
					String uuid = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "Uuid"), PersistentDataType.STRING);
					if (uuid == null) {
						String base64 = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "Base64"), PersistentDataType.STRING);
						if (base64 != null) {
							drop = HeadUtils.getHeadWithBase64(base64, name);
						}
					} else {
						drop = HeadUtils.getHeadWithUUID(UUID.fromString(uuid), name);
					}
					if (drop != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), drop);
						e.setDropItems(false);
					}
				}
			}
		}
		if (is.getAmount() > 0) {
			if (is.hasItemMeta()) {
				ItemMeta im = is.getItemMeta();
				if (im.hasEnchant(PseudoEnchantment.EXCAVATION)) {
					if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
						if (Config.excavation) {
							RayTraceResult rtr = p.rayTraceBlocks(5, FluidCollisionMode.NEVER);
							BlockFace bf = rtr.getHitBlockFace();
							if (bf != null) {
								int broken = 0;
								if (bf == BlockFace.UP) {
									broken = breakUpDown(b, is, p);
									if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
										Block down = b.getRelative(BlockFace.DOWN);
										if (breakBlock(down, is, p))
											broken++;
										broken += breakUpDown(down, is, p);
									}
								} else if (bf == BlockFace.DOWN) {
									broken = breakUpDown(b, is, p);
									if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
										Block up = b.getRelative(BlockFace.UP);
										if (breakBlock(up, is, p))
											broken++;
										broken += breakUpDown(up, is, p);
									}
								} else if (bf == BlockFace.NORTH) {
									broken = breakNorthSouth(b, is, p);
									if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
										Block south = b.getRelative(BlockFace.SOUTH);
										if (breakBlock(south, is, p))
											broken++;
										broken += breakNorthSouth(south, is, p);
									}
								} else if (bf == BlockFace.SOUTH) {
									broken = breakNorthSouth(b, is, p);
									if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
										Block north = b.getRelative(BlockFace.NORTH);
										if (breakBlock(north, is, p))
											broken++;
										broken += breakNorthSouth(north, is, p);
									}
								} else if (bf == BlockFace.EAST) {
									broken = breakEastWest(b, is, p);
									if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
										Block west = b.getRelative(BlockFace.WEST);
										if (breakBlock(west, is, p))
											broken++;
										broken += breakEastWest(west, is, p);
									}
								} else if (bf == BlockFace.WEST) {
									broken = breakEastWest(b, is, p);
									if (im.getEnchantLevel(PseudoEnchantment.EXCAVATION) >= 2) {
										Block east = b.getRelative(BlockFace.EAST);
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
			if (b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD) {
				Skull skull = (Skull) b.getState();
				String name = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "HeadName"), PersistentDataType.STRING);
				if (name != null) {
					ItemStack drop = null;
					String uuid = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "Uuid"), PersistentDataType.STRING);
					if (uuid == null) {
						String base64 = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoEnchants.plugin, "Base64"), PersistentDataType.STRING);
						if (base64 != null) {
							drop = HeadUtils.getHeadWithBase64(base64, name);
						}
					} else {
						drop = HeadUtils.getHeadWithUUID(UUID.fromString(uuid), name);
					}
					if (drop != null) {
						b.setType(Material.AIR);
						b.getWorld().dropItemNaturally(b.getLocation(), drop);
						return true;
					}
				}
			}
			return b.breakNaturally(is);
		}
		return false;
	}

}
