package com.kamillapinski.whitelistplus.access;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class CollectionPlayerWhitelistedChecker implements PlayerWhitelistedChecker {
	private final Collection<String> nicks;

	public CollectionPlayerWhitelistedChecker(Collection<String> nicks) {
		this.nicks = requireNonNull(nicks);
	}

	@Override
	public boolean isWhitelisted(Player player) {
		Objects.requireNonNull(player);

		return nicks.contains(player.getName());
	}

	public Collection<String> getUnderlyingCollection() {
		return nicks;
	}
}
