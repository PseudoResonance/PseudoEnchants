package io.github.pseudoresonance.pseudoenchants.listeners;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import org.bukkit.GameRule;
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
import io.github.pseudoresonance.pseudoenchants.HeadAPI;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;

public class DeathL implements Listener {
	
	private static HashMap<EntityType, SimpleEntry<String, String>> entityMap = new HashMap<EntityType, SimpleEntry<String, String>>();
	
	public static void init() {
		entityMap.put(EntityType.BAT, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU5OWRlZWY5MTlkYjY2YWMyYmQyOGQ2MzAyNzU2Y2NkNTdjN2Y4YjEyYjlkY2E4ZjQxYzNlMGEwNGFjMWNjIn19fQ==", "§eBat Head"));
		entityMap.put(EntityType.BLAZE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA2ZTM0MmY5MGVjNTM4YWFhMTU1MmIyMjRmMjY0YTA0MDg0MDkwMmUxMjZkOTFlY2U2MTM5YWE1YjNjN2NjMyJ9fX0=", "§eBlaze Head"));
		entityMap.put(EntityType.CAT, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVjOTVjMWYyYTUwYjM3ZDYxMjZmNzZlNDYxOGE0Y2M3OTI3OWE4Yzg0NDM3MjA1NGRjM2IyMmVhMWNlMjcifX19", "§eCat Head"));
		entityMap.put(EntityType.CAVE_SPIDER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdiMDcwNjNhNjg3NGZhM2UyMjU0OGUwMjA2MmJkNzMzYzI1ODg1OTI5ODA5NjI0MTgwYWViYjg1MTU1N2Y2YSJ9fX0=", "§eCave Spider Head"));
		entityMap.put(EntityType.CHICKEN, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE2YjhlOTgzODljNTQxYmIzNjQ1Mzg1MGJjYmQxZjdiYzVhNTdkYTYyZGNjNTA1MDYwNDA5NzM3ZWM1YjcyYSJ9fX0=", "§eChicken Head"));
		entityMap.put(EntityType.COD, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg5MmQ3ZGQ2YWFkZjM1Zjg2ZGEyN2ZiNjNkYTRlZGRhMjExZGY5NmQyODI5ZjY5MTQ2MmE0ZmIxY2FiMCJ9fX0=", "§eCod Head"));
		entityMap.put(EntityType.COW, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBlNGU2ZmJmNWYzZGNmOTQ0MjJhMWYzMTk0NDhmMTUyMzY5ZDE3OWRiZmJjZGYwMGU1YmZlODQ5NWZhOTc3In19fQ==", "§eCow Head"));
		entityMap.put(EntityType.DOLPHIN, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmZTdkODAzYTQ1YWEyYWYxOTkzZGYyNTQ0YTI4ZGY4NDlhNzYyNjYzNzE5YmZlZmM1OGJmMzg5YWI3ZjUifX19", "§eDolphin Head"));
		entityMap.put(EntityType.DONKEY, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZiNmMzYzA1MmNmNzg3ZDIzNmEyOTE1ZjgwNzJiNzdjNTQ3NDk3NzE1ZDFkMmY4Y2JjOWQyNDFkODhhIn19fQ==", "§eDonkey Head"));
		entityMap.put(EntityType.DROWNED, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmN2NjZjYxZGJjM2Y5ZmU5YTYzMzNjZGUwYzBlMTQzOTllYjJlZWE3MWQzNGNmMjIzYjNhY2UyMjA1MSJ9fX0=", "§eDrowned Head"));
		entityMap.put(EntityType.ELDER_GUARDIAN, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFkYzRhNmY1M2FmYTExNjAyN2I1MWQ2ZjJlNDMzZWU3YWZhNWQ1OWIyZmZhMDQ3ODBiZTQ2NGZhNWQ2MWEifX19", "§eElder Guardian Head"));
		entityMap.put(EntityType.ENDERMAN, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIwOWEzNzUyNTEwZTkxNGIwYmRjOTA5NmIzOTJiYjM1OWY3YThlOGE5NTY2YTAyZTdmNjZmYWZmOGQ2Zjg5ZSJ9fX0=", "§eEnderman Head"));
		entityMap.put(EntityType.ENDERMITE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTczMDEyN2UzYWM3Njc3MTIyNDIyZGYwMDI4ZDllNzM2OGJkMTU3NzM4YzhjM2NkZGVjYzUwMmU4OTZiZTAxYyJ9fX0=", "§eEndermite Head"));
		entityMap.put(EntityType.EVOKER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk1NDEzNWRjODIyMTM5NzhkYjQ3ODc3OGFlMTIxMzU5MWI5M2QyMjhkMzZkZDU0ZjFlYTFkYTQ4ZTdjYmE2In19fQ==", "§eEvoker Head"));
		entityMap.put(EntityType.FOX, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5NTRhNDJlNjllMDg4MWFlNmQyNGQ0MjgxNDU5YzE0NGEwZDVhOTY4YWVkMzVkNmQzZDczYTNjNjVkMjZhIn19fQ==", "§eFox Head"));
		entityMap.put(EntityType.GHAST, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE0ZTQyZWIxNWEwODgxM2E2YTZmNjFmMTBhYTI4ODAxOWZhMGZhZTEwNmEyOTUzZGRiNDZmNzdlZTJkNzdmIn19fQ==", "§eGhast Head"));
		entityMap.put(EntityType.GUARDIAN, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZiNjc1Y2I1YTdlM2ZkMjVlMjlkYTgyNThmMjRmYzAyMGIzZmE5NTAzNjJiOGJjOGViMjUyZTU2ZTc0In19fQ==", "§eGuardian Head"));
		entityMap.put(EntityType.HORSE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjE5MDI4OTgzMDg3MzBjNDc0NzI5OWNiNWE1ZGE5YzI1ODM4YjFkMDU5ZmU0NmZjMzY4OTZmZWU2NjI3MjkifX19", "§eHorse Head"));
		entityMap.put(EntityType.HUSK, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3NGM2M2M4ZGI1ZjRjYTYyOGQ2OWEzYjFmOGEzNmUyOWQ4ZmQ3NzVlMWE2YmRiNmNhYmI0YmU0ZGIxMjEifX19", "§eHusk Head"));
		entityMap.put(EntityType.ILLUSIONER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYyODgyZGQwOTcyM2U0N2MwYWI5NjYzZWFiMDgzZDZhNTk2OTI3MzcwNjExMGM4MjkxMGU2MWJmOGE4ZjA3ZSJ9fX0=", "§eIllusioner Head"));
		entityMap.put(EntityType.IRON_GOLEM, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM2Y2Q3MjAyYzM0ZTc4ZjMwNzMwOTAzNDlmN2Q5NzNiMjg4YWY1ZTViNzMzNGRkMjQ5MDEwYjNmMjcwNzhmOSJ9fX0=", "§eIron Golem Head"));
		entityMap.put(EntityType.LLAMA, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJiMWVjZmY3N2ZmZTNiNTAzYzMwYTU0OGViMjNhMWEwOGZhMjZmZDY3Y2RmZjM4OTg1NWQ3NDkyMTM2OCJ9fX0=", "§eLlama Head"));
		entityMap.put(EntityType.MAGMA_CUBE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDkwZDYxZThjZTk1MTFhMGEyYjVlYTI3NDJjYjFlZjM2MTMxMzgwZWQ0MTI5ZTFiMTYzY2U4ZmYwMDBkZThlYSJ9fX0=", "§eMagma Cube Head"));
		entityMap.put(EntityType.MULE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTA0ODZhNzQyZTdkZGEwYmFlNjFjZTJmNTVmYTEzNTI3ZjFjM2IzMzRjNTdjMDM0YmI0Y2YxMzJmYjVmNWYifX19", "§eMule Head"));
		entityMap.put(EntityType.MUSHROOM_COW, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTIzY2ZjNTU4MjQ1NGZjZjk5MDZmODQxZmRhMmNjNmFlODk2Y2Y0NTU4MjFjNGFkYTE5OThkZTcwODc3Y2M4NiJ9fX0=", "§eMooshroom Head"));
		entityMap.put(EntityType.OCELOT, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE4YjZiNzk3ODMzNjhkZmUwMDQyOTg1MTEwZGEzNjZmOWM3ODhiNDUwOTdhM2VhNmQwZDlhNzUzZTlmNDJjNiJ9fX0=", "§eOcelot Head"));
		entityMap.put(EntityType.PANDA, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE0ZmY2MjdhNmE2ZjM1ZTFkNzE3ZWJjYjE5MWU0YzdmOTA5NzU0MmRiNTk5ZTcxMDhhZTJjN2RkMzUxM2U1MSJ9fX0=", "§ePanda Head"));
		entityMap.put(EntityType.PARROT, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA3ZGFiMmNiZWJlYTUzOWI2NGQ1YWQyNDZmOWNjYzFmY2RhN2FhOTRiODhlNTlmYzI4Mjk4NTJmNDYwNzEifX19", "§eParrot Head"));
		entityMap.put(EntityType.PHANTOM, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ2ODMwZGE1ZjgzYTNhYWVkODM4YTk5MTU2YWQ3ODFhNzg5Y2ZjZjEzZTI1YmVlZjdmNTRhODZlNGZhNCJ9fX0=", "§ePhantom Head"));
		entityMap.put(EntityType.PIG, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTU2MmEzN2I4NzFmOTY0YmZjM2UxMzExZWE2NzJhYWEwMzk4NGE1ZGM0NzIxNTRhMzRkYzI1YWYxNTdlMzgyYiJ9fX0=", "§ePig Head"));
		entityMap.put(EntityType.PIG_ZOMBIE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE2ZDE2N2M1NzQ0ZWQxNGViYzAyZjQ0N2YzMjYxNDA1OTM2MmI3ZDJlY2I4MDhmZjA2MTY1ZDJjMzQzYmVmMiJ9fX0=", "§eZombie Pigman Head"));
		entityMap.put(EntityType.PILLAGER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlZTZiYjM3Y2JmYzkyYjBkODZkYjVhZGE0NzkwYzY0ZmY0NDY4ZDY4Yjg0OTQyZmRlMDQ0MDVlOGVmNTMzMyJ9fX0=", "§ePillager Head"));
		entityMap.put(EntityType.POLAR_BEAR, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFiMTc4ZjVjZGQ3NTBmMGUzNTY4NjBhYTU1MzkxNTNlYjJhYmVjMWUxNDZjYTU3YzY1ZDI1YTVkZjhmZGZlIn19fQ==", "§ePolar Bear Head"));
		entityMap.put(EntityType.PUFFERFISH, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxNTI4NzZiYzNhOTZkZDJhMjI5OTI0NWVkYjNiZWVmNjQ3YzhhNTZhYzg4NTNhNjg3YzNlN2I1ZDhiYiJ9fX0=", "§ePufferfish Head"));
		entityMap.put(EntityType.RABBIT, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxMTY5YjI2OTRhNmFiYTgyNjM2MDk5MjM2NWJjZGE1YTEwYzg5YTNhYTJiNDhjNDM4NTMxZGQ4Njg1YzNhNyJ9fX0=", "§eRabbit Head"));
		entityMap.put(EntityType.RAVAGER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWNiOWYxMzlmOTQ4OWQ4NmU0MTBhMDZkOGNiYzY3MGM4MDI4MTM3NTA4ZTNlNGJlZjYxMmZlMzJlZGQ2MDE5MyJ9fX0=", "§eRavager Head"));
		entityMap.put(EntityType.SALMON, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFlYjIxYTI1ZTQ2ODA2Y2U4NTM3ZmJkNjY2ODI4MWNmMTc2Y2VhZmU5NWFmOTBlOTRhNWZkODQ5MjQ4NzgifX19", "§eSalmon Head"));
		entityMap.put(EntityType.SHEEP, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2NhMzhjY2Y0MTdlOTljYTlkNDdlZWIxNWE4YTMwZWRiMTUwN2FhNTJiNjc4YzIyMGM3MTdjNDc0YWE2ZmUzZSJ9fX0=", "§eSheep Head"));
		entityMap.put(EntityType.SHULKER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQzNGNiNTdiM2FjNmYzMjJlYzM4ODk2Mzk2MzE2N2NmOGFmYTU3YWRlZDVkYmM1MDEzYjNkZWUwYWIyMiJ9fX0=", "§eShulker Head"));
		entityMap.put(EntityType.SILVERFISH, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5MWRhYjgzOTFhZjVmZGE1NGFjZDJjMGIxOGZiZDgxOWI4NjVlMWE4ZjFkNjIzODEzZmE3NjFlOTI0NTQwIn19fQ==", "§eSilverfish Head"));
		entityMap.put(EntityType.SKELETON_HORSE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ==", "§eSkeleton Horse Head"));
		entityMap.put(EntityType.SLIME, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZjMjdiMDEzZjFiZjMzNDQ4NjllODFlNWM2MTAwMjdiYzQ1ZWM1Yjc5NTE0ZmRjOTZlMDFkZjFiN2UzYTM4NyJ9fX0=", "§eSlime Head"));
		entityMap.put(EntityType.SNOWMAN, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZkZmQxZjc1MzhjMDQwMjU4YmU3YTkxNDQ2ZGE4OWVkODQ1Y2M1ZWY3MjhlYjVlNjkwNTQzMzc4ZmNmNCJ9fX0=", "§eSnow Golem Head"));
		entityMap.put(EntityType.SPIDER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjYxYTQ5NTQxYTgzNmFhOGY0Zjc2ZTBkNGNiMmZmMDQ4ODhjNjJmOTQxMWVhMTBjYmFjZjFmMmE1NDQyNDI0MCJ9fX0=", "§eSpider Head"));
		entityMap.put(EntityType.SQUID, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU4OTEwMWQ1Y2M3NGFhNDU4MDIxYTA2MGY2Mjg5YTUxYTM1YTdkMzRkOGNhZGRmYzNjZGYzYjJjOWEwNzFhIn19fQ==", "§eSquid Head"));
		entityMap.put(EntityType.STRAY, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhkZGY3NmU1NTVkZDVjNGFhOGEwYTVmYzU4NDUyMGNkNjNkNDg5YzI1M2RlOTY5ZjdmMjJmODVhOWEyZDU2In19fQ==", "§eStray Head"));
		entityMap.put(EntityType.TRADER_LLAMA, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQyNDc4MGIzYzVjNTM1MWNmNDlmYjViZjQxZmNiMjg5NDkxZGY2YzQzMDY4M2M4NGQ3ODQ2MTg4ZGI0Zjg0ZCJ9fX0=", "§eTrader Llama Head"));
		entityMap.put(EntityType.TROPICAL_FISH, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTc5ZTQ4ZDgxNGFhM2JjOTg0ZThhNmZkNGZiMTcwYmEwYmI0ODkzZjRiYmViZGU1ZmRmM2Y4Zjg3MWNiMjkyZiJ9fX0=", "§eTropical Fish Head"));
		entityMap.put(EntityType.TURTLE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGE0MDUwZTdhYWNjNDUzOTIwMjY1OGZkYzMzOWRkMTgyZDdlMzIyZjlmYmNjNGQ1Zjk5YjU3MThhIn19fQ==", "§eTurtle Head"));
		entityMap.put(EntityType.VILLAGER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRiZDgzMjgxM2FjMzhlNjg2NDg5MzhkN2EzMmY2YmEyOTgwMWFhZjMxNzQwNDM2N2YyMTRiNzhiNGQ0NzU0YyJ9fX0=", "§eVillager Head"));
		entityMap.put(EntityType.VEX, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJlYzVhNTE2NjE3ZmYxNTczY2QyZjlkNWYzOTY5ZjU2ZDU1NzVjNGZmNGVmZWZhYmQyYTE4ZGM3YWI5OGNkIn19fQ==", "§eVex Head"));
		entityMap.put(EntityType.VINDICATOR, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRlYWVjMzQ0YWIwOTViNDhjZWFkNzUyN2Y3ZGVlNjFiMDYzZmY3OTFmNzZhOGZhNzY2NDJjODY3NmUyMTczIn19fQ==", "§eVindicator Head"));
		entityMap.put(EntityType.WANDERING_TRADER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0=", "§eWandering Trader Head"));
		entityMap.put(EntityType.WITCH, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUxMzkxMzBkN2VmZDQxZmJhZDUzNzM1ZjY0ZjhhZmYyNjViZDdjNTQ5NzcxODljMDJiYWJiZWM0YjBkMDdiIn19fQ==", "§eWitch Head"));
		entityMap.put(EntityType.WITHER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RmNzRlMzIzZWQ0MTQzNjk2NWY1YzU3ZGRmMjgxNWQ1MzMyZmU5OTllNjhmYmI5ZDZjZjVjOGJkNDEzOWYifX19", "§eWither Head"));
		entityMap.put(EntityType.WOLF, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlkMWQzMTEzZWM0M2FjMjk2MWRkNTlmMjgxNzVmYjQ3MTg4NzNjNmM0NDhkZmNhODcyMjMxN2Q2NyJ9fX0=", "§eWolf Head"));
		entityMap.put(EntityType.ZOMBIE_HORSE, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDIyOTUwZjJkM2VmZGRiMThkZTg2ZjhmNTVhYzUxOGRjZTczZjEyYTZlMGY4NjM2ZDU1MWQ4ZWI0ODBjZWVjIn19fQ==", "§eZombie Horse Head"));
		entityMap.put(EntityType.ZOMBIE_VILLAGER, new SimpleEntry<String, String>("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTYyMjQ5NDEzMTRiY2EyZWJiYjY2YjEwZmZkOTQ2ODBjYzk4YzM0MzVlZWI3MWEyMjhhMDhmZDQyYzI0ZGIifX19", "§eZombie Villager Head"));
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		LivingEntity target = e.getEntity();
		Player killer = target.getKiller();
		if (target.getWorld().getGameRuleValue(GameRule.DO_MOB_LOOT)) {
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
							} else if (target.getType() == EntityType.ZOMBIE || target.getType() == EntityType.GIANT) {
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
									SimpleEntry<String, String> entry = entityMap.get(target.getType());
									head = HeadAPI.getHeadWithBase64(entry.getKey(), entry.getValue());
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

}
