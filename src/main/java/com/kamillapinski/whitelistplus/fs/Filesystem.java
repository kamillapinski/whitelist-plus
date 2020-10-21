package com.kamillapinski.whitelistplus.fs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * This interface lets you interact with the filesystem.
 */
public interface Filesystem {
	/**
	 * Reads a files line-by-line.
	 * @param path Path to the file
	 * @return File's lines as {@link Stream}
	 * @throws IOException when an filesystem error occurs
	 */
	Stream<String> lines(Path path) throws IOException;

	/**
	 * Creates a file if it does not exist yet.
	 * @param path Path to the file to be created
	 * @throws IOException when a filesystem error occurs
	 */
	void createFileIfNotExists(Path path) throws IOException;
}
