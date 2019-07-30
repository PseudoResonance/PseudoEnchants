package io.github.pseudoresonance.pseudoenchants.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoenchants.PseudoEnchants;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class ResetSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoenchants.reset")) {
				try {
					File conf = new File(PseudoEnchants.plugin.getDataFolder(), "config.yml");
					conf.delete();
					PseudoEnchants.plugin.saveDefaultConfig();
					PseudoEnchants.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoEnchants.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoEnchants.getConfigOptions().reloadConfig();
				PseudoEnchants.message.sendPluginMessage(sender, "Plugin config reset!");
				return true;
			} else {
				PseudoEnchants.message.sendPluginError(sender, Errors.NO_PERMISSION, "reset the config!");
				return false;
			}
		} else {
			try {
				File conf = new File(PseudoEnchants.plugin.getDataFolder(), "config.yml");
				conf.delete();
				PseudoEnchants.plugin.saveDefaultConfig();
				PseudoEnchants.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoEnchants.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoEnchants.getConfigOptions().reloadConfig();
			PseudoEnchants.message.sendPluginMessage(sender, "Plugin config reset!");
			return true;
		}
	}

}
