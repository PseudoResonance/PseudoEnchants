package io.github.pseudoresonance.pseudoenchants.listeners;

import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.pseudoresonance.pseudoenchants.Config;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;

public class DeathL implements Listener {
	
	//private static HashMap<EntityType, UUID> entityUUIDMap = new HashMap<EntityType, UUID>();
	private static HashMap<EntityType, String> entityMap = new HashMap<EntityType, String>();
	
	public static void init() {
		/**entityUUIDMap.put(EntityType.BLAZE, UUID.fromString("4c38ed11-596a-4fd4-ab1d-26f386c1cbac"));
		entityUUIDMap.put(EntityType.CAVE_SPIDER, UUID.fromString("cab28771-f0cd-4fe7-b129-02c69eba79a5"));
		entityUUIDMap.put(EntityType.CHICKEN, UUID.fromString("92deafa9-4307-42d9-b003-88601598d6c0"));
		entityUUIDMap.put(EntityType.COW, UUID.fromString("f159b274-c22e-4340-b7c1-52abde147713"));
		entityUUIDMap.put(EntityType.ENDERMAN, UUID.fromString("40ffb372-12f6-4678-b3f2-2176bf56dd4b"));
		entityUUIDMap.put(EntityType.GHAST, UUID.fromString("063085a6-797f-4785-be1a-21cd7580f752"));
		entityUUIDMap.put(EntityType.IRON_GOLEM, UUID.fromString("757f90b2-2344-4b8d-8dac-824232e2cece"));
		entityUUIDMap.put(EntityType.MAGMA_CUBE, UUID.fromString("0972bdd1-4b86-49fb-9ecc-a353f8491a51"));
		entityUUIDMap.put(EntityType.MUSHROOM_COW, UUID.fromString("a46817d6-73c5-4f3f-b712-af6b3ff47b96"));
		entityUUIDMap.put(EntityType.OCELOT, UUID.fromString("1bee9df5-4f71-42a2-bf52-d97970d3fea3"));
		entityUUIDMap.put(EntityType.PIG, UUID.fromString("8b57078b-f1bd-45df-83c4-d88d16768fbe"));
		entityUUIDMap.put(EntityType.PIG_ZOMBIE, UUID.fromString("18a2bb50-334a-4084-9184-2c380251a24b"));
		entityUUIDMap.put(EntityType.SHEEP, UUID.fromString("dfaad551-4e7e-45a1-a6f7-c6fc5ec823ac"));
		entityUUIDMap.put(EntityType.SLIME, UUID.fromString("870aba93-40e8-48b3-89c5-32ece00d6630"));
		entityUUIDMap.put(EntityType.SPIDER, UUID.fromString("5ad55f34-41b6-4bd2-9c32-18983c635936"));
		entityUUIDMap.put(EntityType.SQUID, UUID.fromString("72e64683-e313-4c36-a408-c66b64e94af5"));
		entityUUIDMap.put(EntityType.VILLAGER, UUID.fromString("bd482739-767c-45dc-a1f8-c33c40530952"));**/
		entityMap.put(EntityType.BLAZE, "MHF_Blaze");
		entityMap.put(EntityType.CAVE_SPIDER, "MHF_CaveSpider");
		entityMap.put(EntityType.CHICKEN, "MHF_Chicken");
		entityMap.put(EntityType.COW, "MHF_Cow");
		entityMap.put(EntityType.ENDERMAN, "MHF_Enderman");
		entityMap.put(EntityType.GHAST, "MHF_Ghast");
		entityMap.put(EntityType.IRON_GOLEM, "MHF_Golem");
		entityMap.put(EntityType.MAGMA_CUBE, "MHF_LavaSlime");
		entityMap.put(EntityType.MUSHROOM_COW, "MHF_MushroomCow");
		entityMap.put(EntityType.OCELOT, "MHF_Ocelot");
		entityMap.put(EntityType.PIG, "MHF_Pig");
		entityMap.put(EntityType.PIG_ZOMBIE, "MHF_PigZombie");
		entityMap.put(EntityType.SHEEP, "MHF_Sheep");
		entityMap.put(EntityType.SLIME, "MHF_Slime");
		entityMap.put(EntityType.SPIDER, "MHF_Spider");
		entityMap.put(EntityType.SQUID, "MHF_Squid");
		entityMap.put(EntityType.VILLAGER, "MHF_Villager");
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		LivingEntity target = e.getEntity();
		Player killer = target.getKiller();
		if (killer != null) {
			ItemStack weapon = killer.getInventory().getItemInMainHand();
			if (weapon.getAmount() > 0) {
				if (weapon.containsEnchantment(PseudoEnchantment.BEHEADING)) {
					int level = weapon.getEnchantmentLevel(PseudoEnchantment.BEHEADING);
					double chance = level * Config.beheadingMultiplier;
					if (Math.random() <= chance) {
						List<ItemStack> drops = e.getDrops();
						ItemStack head = null;
						if (target.getType() == EntityType.SKELETON) {
							head = new ItemStack(Material.SKELETON_SKULL);
						} else if (target.getType() == EntityType.WITHER_SKELETON) {
							head = new ItemStack(Material.WITHER_SKELETON_SKULL);
						} else if (target.getType() == EntityType.ZOMBIE) {
							head = new ItemStack(Material.ZOMBIE_HEAD);
						} else if (target.getType() == EntityType.CREEPER) {
							head = new ItemStack(Material.CREEPER_HEAD);
						} else if (target.getType() == EntityType.ENDER_DRAGON) {
							head = new ItemStack(Material.DRAGON_HEAD);
						} else if (target.getType() == EntityType.PLAYER) {
							head = new ItemStack(Material.PLAYER_HEAD);
							SkullMeta meta = (SkullMeta) head.getItemMeta();
							meta.setOwningPlayer((Player) target);
							head.setItemMeta(meta);
						} else {
							if (entityMap.containsKey(target.getType())) {
								head = new ItemStack(Material.PLAYER_HEAD);
								SkullMeta meta = (SkullMeta) head.getItemMeta();
								meta.setOwner(entityMap.get(target.getType()));
								head.setItemMeta(meta);
							}
						}
						if (head != null) {
							drops.add(head);
						}
					}
				}
			}
		}
	}

}
