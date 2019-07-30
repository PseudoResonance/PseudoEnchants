package io.github.pseudoresonance.pseudoenchants;

import org.bukkit.configuration.file.FileConfiguration;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.data.PluginConfig;

public class Config extends PluginConfig {
	
	public static boolean excavation = true;
	
	public void firstInit() {
		FileConfiguration fc = PseudoEnchants.plugin.getConfig();

		excavation = PluginConfig.getBoolean(fc, "Excavation", excavation);
	}
	
	public void reloadConfig() {
	}
	
	public Config(PseudoPlugin plugin) {
		super(plugin);
	}

}