package io.github.pseudoresonance.pseudoenchants;

import java.lang.reflect.Field;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.CommandDescription;
import io.github.pseudoresonance.pseudoapi.bukkit.HelpSC;
import io.github.pseudoresonance.pseudoapi.bukkit.MainCommand;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoUpdater;
import io.github.pseudoresonance.pseudoenchants.commands.ReloadLocalizationSC;
import io.github.pseudoresonance.pseudoenchants.commands.ReloadSC;
import io.github.pseudoresonance.pseudoenchants.commands.ResetLocalizationSC;
import io.github.pseudoresonance.pseudoenchants.commands.ResetSC;
import io.github.pseudoresonance.pseudoenchants.completers.PseudoEnchantsTC;
import io.github.pseudoresonance.pseudoenchants.enchantments.PseudoEnchantment;
import io.github.pseudoresonance.pseudoenchants.listeners.BlockL;
import io.github.pseudoresonance.pseudoenchants.listeners.DeathL;
import io.github.pseudoresonance.pseudoenchants.listeners.EnchantL;

public class PseudoEnchants extends PseudoPlugin {

	public static PseudoEnchants plugin;

	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;

	private static Config config;
	
	public static boolean isWorldGuardLoaded = false;
	
	@SuppressWarnings("unused")
	private static Metrics metrics = null;
	
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
		initializeMetrics();
	}

	public void onDisable() {
		super.onDisable();
	}
	
	private void initializeMetrics() {
		metrics = new Metrics(this);
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
		subCommands.put("reloadlocalization", new ReloadLocalizationSC());
		subCommands.put("reset", new ResetSC());
		subCommands.put("resetlocalization", new ResetLocalizationSC());
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
		commandDescriptions.add(new CommandDescription("pseudoenchants", "pseudoenchants.pseudoenchants_help", ""));
		commandDescriptions.add(new CommandDescription("pseudoenchants help", "pseudoenchants.pseudoenchants_help_help", ""));
		commandDescriptions.add(new CommandDescription("pseudoenchants reload", "pseudoenchants.pseudoenchants_reload_help", "pseudoenchants.reload"));
		commandDescriptions.add(new CommandDescription("pseudoutils reload", "pseudoenchants.pseudoenchants_reloadlocalization_help", "pseudoenchants.reloadlocalization"));
		commandDescriptions.add(new CommandDescription("pseudoenchants reset", "pseudoenchants.pseudoenchants_reset_help", "pseudoenchants.reset"));
		commandDescriptions.add(new CommandDescription("pseudoutils reset", "pseudoenchants.pseudoenchants_resetlocalization_help", "pseudoenchants.resetlocalization"));
	}
	
	private void registerEnchantments() {
		getChat().sendConsolePluginMessage(LanguageManager.getLanguage().getMessage("pseudoenchants.beginning_registration"));
		setAllowEnchantmentRegistration();
		boolean success = PseudoEnchantment.registerEnchantments();
		if (success) {
			getChat().sendConsolePluginMessage(LanguageManager.getLanguage().getMessage("pseudoenchants.completed_registration"));
		} else {
			getChat().sendConsolePluginError(Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudoenchants.error_completed_registration"));
		}
	}
	
	private void setAllowEnchantmentRegistration() {
		try {
			Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingNew.setAccessible(true);
			acceptingNew.setBoolean(null, true);
			getChat().sendConsolePluginMessage(LanguageManager.getLanguage().getMessage("pseudoenchants.accepting_new"));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			getChat().sendConsolePluginError(Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudoenchants.error_accepting_new"));
			e.printStackTrace();
		}
	}

}