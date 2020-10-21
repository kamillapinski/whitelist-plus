package com.kamillapinski.whitelistplus.manager;

import com.kamillapinski.whitelistplus.access.PlayerWhitelistedChecker;
import com.kamillapinski.whitelistplus.access.CollectionPlayerWhitelistedChecker;
import com.kamillapinski.whitelistplus.nicks.NicksSource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.Objects.requireNonNull;

public class NicksSourceWhitelistManager implements ReloadableWhitelistManager {
	private final CollectionPlayerWhitelistedChecker accessChecker = new CollectionPlayerWhitelistedChecker(new ConcurrentSkipListSet<>());
	private final NicksSource nicksSource;

	public NicksSourceWhitelistManager(NicksSource nicksSource) {
		this.nicksSource = requireNonNull(nicksSource);
	}

	@Override
	public PlayerWhitelistedChecker getWhitelistedChecker() {
		return accessChecker;
	}

	@Override
	public CompletableFuture<Void> reload() {
		return nicksSource.fetchNicks().thenAccept(nicksSet -> {
			Collection<String> nicks = accessChecker.getUnderlyingCollection();

			nicks.clear();
			nicks.addAll(nicksSet);
		});
	}
}
