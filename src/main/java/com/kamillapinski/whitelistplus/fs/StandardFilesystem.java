package com.kamillapinski.whitelistplus.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Basic {@link Filesystem} implementation.
 * It uses the {@link Files} class from the standard library.
 */
public class StandardFilesystem implements Filesystem {
	private static final Filesystem INSTANCE = new StandardFilesystem();

	public static Filesystem getInstance() {
		return INSTANCE;
	}

	private StandardFilesystem() {
	}


	@Override
	public Stream<String> lines(Path path) throws IOException {
		return Files.lines(path);
	}

	@Override
	public void createFileIfNotExists(Path path) throws IOException {
		if (Files.notExists(path)) {
			Files.createFile(path);
		}
	}
}
