package com.kamillapinski.whitelistplus.listeners;

import com.kamillapinski.whitelistplus.access.PlayerWhitelistedChecker;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

class PlayerLoginListenerTest {
	private static final Collection<String> ALLOWED_NICKS = Set.of("12345", "abbas", "papajak");
	private final PlayerLoginListener playerLoginListener;

	private Player createPlayer(String nick) {
		Player player = mock(Player.class);

		when(player.getName()).thenReturn(nick);

		return player;
	}

	@SuppressWarnings("ConstantConditions")
	private PlayerLoginEvent playerLoginEvent(String nick) {
		return spy(new PlayerLoginEvent(
			createPlayer(nick),
			null,
			null,
			null
		));
	}

	PlayerLoginListenerTest() {
		PlayerLoginListener.Config config = new PlayerLoginListener.Config("NOT_WHITELISTED");
		PlayerWhitelistedChecker playerWhitelistedChecker = player -> ALLOWED_NICKS.contains(player.getName());
		playerLoginListener = new PlayerLoginListener(playerWhitelistedChecker, config, mock(Logger.class));
	}

	@Test
	void disallowsNotWhitelisted() {
		Set.of("sdfsdf", "vce4", "#4scv")
		   .forEach(notWhitelistedNick -> {
			   PlayerLoginEvent event = playerLoginEvent(notWhitelistedNick);

			   playerLoginListener.onPlayerLogin(event);

			   verify(event).disallow(eq(PlayerLoginEvent.Result.KICK_WHITELIST), eq(Component.text("NOT_WHITELISTED")));
		   });
	}

	@Test
	void allowsWhitelisted() {
		ALLOWED_NICKS.forEach(allowedNick -> {
			PlayerLoginEvent event = playerLoginEvent(allowedNick);

			playerLoginListener.onPlayerLogin(event);

			verify(event, never()).disallow(any(), Mockito.<Component>any());
		});
	}
}