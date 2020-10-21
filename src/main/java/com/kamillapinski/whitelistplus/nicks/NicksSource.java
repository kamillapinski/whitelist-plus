package com.kamillapinski.whitelistplus.nicks;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface NicksSource {
	CompletableFuture<Set<String>> fetchNicks();
}
