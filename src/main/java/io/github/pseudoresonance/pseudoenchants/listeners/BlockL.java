package io.github.pseudoresonance.pseudoenchants.listeners;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.HeadUtils;
import io.github.pseudoresonance.pseudoenchants.Config;
import io.github.pseudoresonance.pseudoenchants.PseudoEnchants;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;

public class BlockL implements Listener {

	private static boolean setup = false;
	private static Class<?> worldGuardPlugin = null;
	private static Object worldGuardPluginObj = null;
	private static Method wrapPlayer = null;
	private static Class<?> localPlayer = null;
	private static Method getWorld = null;
	private static Class<?> location = null;
	private static Constructor<?> locationConst = null;
	private static Class<?> extent = null;
	private static Class<?> world = null;
	private static Class<?> worldGuard = null;
	private static Object worldGuardObj = null;
	private static Method getPlatform = null;
	private static Class<?> worldGuardPlatform = null;
	private static Method getRegionContainer = null;
	private static Class<?> regionContainer = null;
	private static Method createQuery = null;
	private static Class<?> regionQuery = null;
	private static Method getSessionManager = null;
	private static Class<?> sessionManager = null;
	private static Class<?> stateFlag = null;
	private static Method hasBypass = null;
	private static Method testBuild = null;

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
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
				try {
					if (!setup) {
						worldGuardPlugin = Class.forName("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
						Method worldGuardPluginInst = worldGuardPlugin.getMethod("inst");
						worldGuardPluginInst.setAccessible(true);
						worldGuardPluginObj = worldGuardPluginInst.invoke(null);
						wrapPlayer = worldGuardPluginObj.getClass().getDeclaredMethod("wrapPlayer", Player.class);
						wrapPlayer.setAccessible(true);
						localPlayer = Class.forName("com.sk89q.worldguard.LocalPlayer");
						getWorld = localPlayer.getMethod("getWorld");
						getWorld.setAccessible(true);
						location = Class.forName("com.sk89q.worldedit.util.Location");
						extent = Class.forName("com.sk89q.worldedit.extent.Extent");
						world = Class.forName("com.sk89q.worldedit.world.World");
						locationConst = location.getConstructor(extent, double.class, double.class, double.class);
						worldGuard = Class.forName("com.sk89q.worldguard.WorldGuard");
						Method worldGuardInstance = worldGuard.getMethod("getInstance");
						worldGuardInstance.setAccessible(true);
						worldGuardObj = worldGuardInstance.invoke(null);
						getPlatform = worldGuardObj.getClass().getMethod("getPlatform");
						getPlatform.setAccessible(true);
						worldGuardPlatform = Class.forName("com.sk89q.worldguard.internal.platform.WorldGuardPlatform");
						getRegionContainer = worldGuardPlatform.getMethod("getRegionContainer");
						getRegionContainer.setAccessible(true);
						regionContainer = Class.forName("com.sk89q.worldguard.protection.regions.RegionContainer");
						createQuery = regionContainer.getMethod("createQuery");
						createQuery.setAccessible(true);
						regionQuery = Class.forName("com.sk89q.worldguard.protection.regions.RegionQuery");
						getSessionManager = worldGuardPlatform.getMethod("getSessionManager");
						getSessionManager.setAccessible(true);
						sessionManager = Class.forName("com.sk89q.worldguard.session.SessionManager");
						stateFlag = Class.forName("com.sk89q.worldguard.protection.flags.StateFlag");
						hasBypass = sessionManager.getMethod("hasBypass", localPlayer, world);
						for (Method m : regionQuery.getMethods())
							if (m.getName().equals("testBuild") && m.getParameterTypes()[0] == location && m.getParameterTypes()[1] == localPlayer) {
								testBuild = m;
								break;
							}
						setup = true;
					}
					Object lp = wrapPlayer.invoke(worldGuardPluginObj, p);
					Object world = getWorld.invoke(lp);
					Object loc = locationConst.newInstance(world, b.getX(), b.getY(), b.getZ());
					Object platform = getPlatform.invoke(worldGuardObj);
					Object cont = getRegionContainer.invoke(platform);
					Object query = createQuery.invoke(cont);
					Object sessionManagerObj = getSessionManager.invoke(platform);
					boolean bypass = (boolean) hasBypass.invoke(sessionManagerObj, lp, world);
					boolean build = (boolean) testBuild.invoke(query, loc, lp, Array.newInstance(stateFlag, 0));
					if (!bypass && !build) {
						return false;
					}
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
					PseudoEnchants.plugin.getChat().sendPluginError(p, Errors.CUSTOM, LanguageManager.getLanguage(p).getMessage("pseudoenchants.error_checking_permissions"));
					e.printStackTrace();
					return false;
				}
			}
			if (b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD) {
				Skull skull = (Skull) b.getState();
				String name = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoAPI.plugin, "HeadName"), PersistentDataType.STRING);
				if (name != null) {
					ItemStack drop = null;
					String uuid = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoAPI.plugin, "Uuid"), PersistentDataType.STRING);
					if (uuid == null) {
						String base64 = skull.getPersistentDataContainer().get(new NamespacedKey(PseudoAPI.plugin, "Base64"), PersistentDataType.STRING);
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
