package com.kamillapinski.whitelistplus.access;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionPlayerWhitelistedCheckerTest {
	private final Set<String> allowedNicks = Set.of("abc", "12345", "bedf");
	private final PlayerWhitelistedChecker playerWhitelistedChecker = new CollectionPlayerWhitelistedChecker(allowedNicks);

	@Test
	public void checksWhitelisted() {
		Player playerMock = mock(Player.class);

		allowedNicks.forEach(nick -> {
			when(playerMock.getName()).thenReturn(nick);

			assertTrue(
				playerWhitelistedChecker.isWhitelisted(playerMock),
				String.format("Player %s should be whitelisted", nick)
			);
		});
	}

	@Test
	public void checkNotWhitelisted() {
		Player playerMock = mock(Player.class);

		Set.of("542", "xcwasd", "sDFB3", "kamilw34")
		   .forEach(wrongNick -> {
			   when(playerMock.getName()).thenReturn(wrongNick);

			   assertFalse(
				   playerWhitelistedChecker.isWhitelisted(playerMock),
				   String.format("PLayer %s should not be whitelisted", wrongNick)
			   );
		   });
	}
}