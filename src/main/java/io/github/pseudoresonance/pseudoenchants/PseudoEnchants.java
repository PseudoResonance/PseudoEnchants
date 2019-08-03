package io.github.pseudoresonance.pseudoenchants;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import io.github.pseudoresonance.pseudoapi.bukkit.CommandDescription;
import io.github.pseudoresonance.pseudoapi.bukkit.HelpSC;
import io.github.pseudoresonance.pseudoapi.bukkit.MainCommand;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoUpdater;
import io.github.pseudoresonance.pseudoenchants.commands.ReloadSC;
import io.github.pseudoresonance.pseudoenchants.commands.ResetSC;
import io.github.pseudoresonance.pseudoenchants.completers.PseudoEnchantsTC;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;
import io.github.pseudoresonance.pseudoenchants.listeners.BlockL;
import io.github.pseudoresonance.pseudoenchants.listeners.DeathL;
import io.github.pseudoresonance.pseudoenchants.listeners.EnchantL;

public class PseudoEnchants extends PseudoPlugin {

	public static PseudoPlugin plugin;
	public static Message message;

	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;

	private static Config config;
	
	public static boolean isWorldGuardLoaded = false;
	
	public void onLoad() {
		PseudoUpdater.registerPlugin(this);
	}

	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		plugin = this;
		config = new Config(this);
		config.updateConfig();
		config.firstInit();
		config.reloadConfig();
		message = new Message(this);
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		registerEnchantments();
		PseudoAPI.registerConfig(config);
		DeathL.init();
		if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
			isWorldGuardLoaded = true;
		}
	}

	public static Config getConfigOptions() {
		return PseudoEnchants.config;
	}

	private void initializeCommands() {
		this.getCommand("pseudoenchants").setExecutor(mainCommand);
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reset", new ResetSC());
	}

	private void initializeTabcompleters() {
		this.getCommand("pseudoenchants").setTabCompleter(new PseudoEnchantsTC());
	}

	private void initializeListeners() {
		getServer().getPluginManager().registerEvents(new BlockL(), this);
		getServer().getPluginManager().registerEvents(new EnchantL(), this);
		getServer().getPluginManager().registerEvents(new DeathL(), this);
	}

	private void setCommandDescriptions() {
		commandDescriptions.add(new CommandDescription("pseudoenchants", "Shows PseudoEnchants information", ""));
		commandDescriptions.add(new CommandDescription("pseudoenchants help", "Shows PseudoEnchants commands", ""));
		commandDescriptions.add(new CommandDescription("pseudoenchants reload", "Reloads PseudoEnchants config", "pseudoenchants.reload"));
		commandDescriptions.add(new CommandDescription("pseudoenchants reset", "Resets PseudoEnchants config", "pseudoenchants.reset"));
		commandDescriptions.add(new CommandDescription("enchant <enchantment> <level>", "Enchants an item", "pseudoenchants.enchant", false));
	}
	
	private void registerEnchantments() {
		message.sendPluginMessage(Bukkit.getServer().getConsoleSender(), "Beginning enchantment registration!");
		setAllowEnchantmentRegistration();
		boolean success = PseudoEnchantment.registerEnchantments();
		if (success) {
			message.sendPluginMessage(Bukkit.getServer().getConsoleSender(), "Successfully completed enchantment registration!");
		} else {
			message.sendPluginError(Bukkit.getServer().getConsoleSender(), Errors.CUSTOM, "Enchantment registration could not be completed successfully! Please check the logs for more information!");
		}
	}
	
	private static void setAllowEnchantmentRegistration() {
		try {
			Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingNew.setAccessible(true);
			acceptingNew.setBoolean(null, true);
			message.sendPluginMessage(Bukkit.getServer().getConsoleSender(), "Set Bukkit to accept new enchantments!");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			message.sendPluginError(Bukkit.getServer().getConsoleSender(), Errors.CUSTOM, "Could not set Bukkit to allow new enchantments!");
			e.printStackTrace();
		}
	}

}