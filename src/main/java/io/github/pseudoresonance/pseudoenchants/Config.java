package io.github.pseudoresonance.pseudoenchants;

import org.bukkit.configuration.file.FileConfiguration;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.data.PluginConfig;

public class Config extends PluginConfig {
	
	public static boolean excavation = true;
	public static boolean beheading = true;
	
	public static double beheadingMultiplier = 0.1;
	
	public void firstInit() {
		FileConfiguration fc = PseudoEnchants.plugin.getConfig();

		excavation = PluginConfig.getBoolean(fc, "Excavation", excavation);
		beheading = PluginConfig.getBoolean(fc, "Beheading", beheading);
		
		beheadingMultiplier = PluginConfig.getDouble(fc, "BeheadingMultiplier", beheadingMultiplier);
	}
	
	public void reloadConfig() {
	}
	
	public Config(PseudoPlugin plugin) {
		super(plugin);
	}

}