package com.kamillapinski.whitelistplus.access;

import org.bukkit.entity.Player;

public interface PlayerWhitelistedChecker {
	boolean isWhitelisted(Player player);
}
