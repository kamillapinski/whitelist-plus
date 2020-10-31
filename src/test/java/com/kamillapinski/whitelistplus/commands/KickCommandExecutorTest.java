package com.kamillapinski.whitelistplus.commands;

import com.kamillapinski.whitelistplus.access.PlayerWhitelistedChecker;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class KickCommandExecutorTest {
	private static final Collection<String> ALLOWED_NICKS = Set.of("cba", "123");

	private final CommandSender commandSender;
	private final Server server;
	private final PlayerWhitelistedChecker playerWhitelistedChecker = player -> ALLOWED_NICKS.contains(player.getName());
	private final KickCommandExecutor kickCommandExecutor;

	private final Collection<Player> onlinePlayers = new ArrayList<>();

	KickCommandExecutorTest() {
		this.commandSender = mock(CommandSender.class);
		this.server = mock(Server.class);

		kickCommandExecutor = new KickCommandExecutor(
			server,
			mock(MemoryConfiguration.class),
			playerWhitelistedChecker,
			mock(Logger.class)
		);
	}

	@BeforeEach
	public void setUp() {
		onlinePlayers.clear();

		when(server.getOnlinePlayers()).then(invocation -> onlinePlayers);

		onlinePlayers.addAll(Arrays.asList(
			createPlayer("abc"),
			createPlayer("cba"),
			createPlayer("123"),
			createPlayer("321")
		));

		kickCommandExecutor.onCommand(commandSender, null, null, null);
	}

	@Test
	public void kicksNotWhitelisted() {
		assertEquals(2, onlinePlayers.size(), "There should be 2 players left");

		ALLOWED_NICKS.forEach(nick -> {
			assertTrue(
				onlinePlayers.stream().anyMatch(p -> p.getName().equals(nick)),
				String.format("Player %s should be whitelisted", nick)
			);
		});
	}

	private Player createPlayer(String nick) {
		Player result = mock(Player.class);

		doAnswer(invocation -> {
			onlinePlayers.removeIf(p -> p.getName().equals(nick));
			return null;
		}).when(result).kickPlayer(any());

		when(result.getName()).thenReturn(nick);
		when(result.getDisplayName()).thenReturn(nick);

		return result;
	}
}