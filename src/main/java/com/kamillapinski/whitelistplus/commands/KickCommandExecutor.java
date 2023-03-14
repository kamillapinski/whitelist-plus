package com.kamillapinski.whitelistplus.commands;

import com.kamillapinski.whitelistplus.access.PlayerWhitelistedChecker;
import com.kamillapinski.whitelistplus.config.ConfigurationEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class KickCommandExecutor implements CommandExecutor {
	private final Server server;
	private final MemoryConfiguration configuration;
	private final PlayerWhitelistedChecker playerWhitelistedChecker;
	private final Logger logger;

	public KickCommandExecutor(
		Server server,
		MemoryConfiguration configuration,
		PlayerWhitelistedChecker playerWhitelistedChecker,
		Logger logger
	) {
		this.server = server;
		this.configuration = configuration;
		this.playerWhitelistedChecker = playerWhitelistedChecker;
		this.logger = logger;
	}

	@Override
	public boolean onCommand(
		CommandSender sender,
		Command command,
		String label,
		String[] args
	) {
		List<Player> playersToKick = getPlayersToKick();

		if (playersToKick.isEmpty()) {
			logger.info("No one kicked");
			sender.sendMessage("No one kicked");
		} else {
			kickPlayers(sender, playersToKick);
		}
		return true;
	}

	private void kickPlayers(CommandSender sender, Collection<Player> playersToKick) {
		for (Player player : playersToKick) {
			player.kick(Component.text(ConfigurationEntry.NOT_WHITELISTED_MESSAGE.getString(configuration)));
			logger.info("Kicked player " + player.getName());
		}

		sender.sendMessage("Kicked " + playersToKick.size() + " players");
	}

	private List<Player> getPlayersToKick() {
		return server.getOnlinePlayers()
		             .stream()
		             .filter(player -> !playerWhitelistedChecker.isWhitelisted(player))
		             .collect(Collectors.toList());
	}
}
