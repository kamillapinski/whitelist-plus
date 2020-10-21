package com.kamillapinski.whitelistplus.manager;

import com.kamillapinski.whitelistplus.access.PlayerWhitelistedChecker;

import java.util.concurrent.CompletableFuture;

public interface ReloadableWhitelistManager {
	PlayerWhitelistedChecker getWhitelistedChecker();

	CompletableFuture<Void> reload();
}
