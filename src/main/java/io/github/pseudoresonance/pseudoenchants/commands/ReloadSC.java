package io.github.pseudoresonance.pseudoenchants.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoenchants.PseudoEnchants;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("pseudoenchants.reload")) {
				try {
					PseudoEnchants.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoEnchants.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoEnchants.getConfigOptions().reloadConfig();
				PseudoEnchants.message.sendPluginMessage(sender, "Plugin config reloaded!");
				return true;
			} else {
				PseudoEnchants.message.sendPluginError(sender, Errors.NO_PERMISSION, "reload the config!");
				return false;
			}
		} else {
			try {
				PseudoEnchants.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoEnchants.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoEnchants.getConfigOptions().reloadConfig();
			PseudoEnchants.message.sendPluginMessage(sender, "Plugin config reloaded!");
			return true;
		}
	}

}
