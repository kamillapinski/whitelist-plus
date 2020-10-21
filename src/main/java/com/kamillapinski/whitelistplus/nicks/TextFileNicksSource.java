package com.kamillapinski.whitelistplus.nicks;

import com.kamillapinski.whitelistplus.fs.Filesystem;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * {@link NicksSource} implementation which reads allowed nicks from a simple text file.
 */
public class TextFileNicksSource implements NicksSource {
	private final Filesystem filesystem;
	private final Path filePath;

	public TextFileNicksSource(Filesystem filesystem, Path filePath) {
		this.filesystem = requireNonNull(filesystem);
		this.filePath = requireNonNull(filePath);
	}

	private void createFileIfNeeded() {
		try {
			filesystem.createFileIfNotExists(filePath);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private Set<String> loadNicks() {
		try (Stream<String> linesStream = filesystem.lines(filePath)) {
			return linesStream.map(String::trim)
			                  .filter(line -> !line.trim().isEmpty())
			                  .collect(Collectors.toSet());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@Override
	public CompletableFuture<Set<String>> fetchNicks() {
		createFileIfNeeded();

		Set<String> nicks = loadNicks();
		return CompletableFuture.completedFuture(nicks);
	}
}
