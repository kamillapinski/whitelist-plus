package com.kamillapinski.whitelistplus.commands;

import com.kamillapinski.whitelistplus.manager.ReloadableWhitelistManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class ReloadWhitelistCommandExecutor implements CommandExecutor {
	private final ReloadableWhitelistManager reloadableWhitelistManager;
	private final Logger logger;

	public ReloadWhitelistCommandExecutor(
		ReloadableWhitelistManager reloadableWhitelistManager,
		Logger logger
	) {
		this.reloadableWhitelistManager = requireNonNull(reloadableWhitelistManager);
		this.logger = requireNonNull(logger);
	}

	@Override
	public boolean onCommand(
		CommandSender sender,
		Command command,
		String label,
		String[] args
	) {
		sender.sendMessage("Attempting to reload the whitelist...");
		logger.info("Attempting to reload the whitelist");

		reloadableWhitelistManager.reload()
		                          .thenRun(() -> {
			                          logger.info("Reloaded the whitelist");
			                          sender.sendMessage("Reloaded the whitelist");
		                          });

		return true;
	}
}
