package com.kamillapinski.whitelistplus.nicks;

import com.kamillapinski.whitelistplus.fs.Filesystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextFileNicksSourceTest {
	private static final Path TEST_FILE_PATH = Paths.get("/test/file.txt");

	private final Filesystem mockFilesystem = new Filesystem() {
		public boolean fileCreated = false;

		@Override
		public Stream<String> lines(Path path) throws IOException {
			if (path.equals(TEST_FILE_PATH)) {
				if (fileCreated) {
					return Stream.of("A", "\t  ", "B", "", "C");
				} else {
					Assertions.fail("The file does not exist yet: " + path);

					return Stream.empty();
				}
			} else {
				Assertions.fail("No such file: " + path);

				return Stream.empty();
			}
		}

		@Override
		public void createFileIfNotExists(Path path) throws IOException {
			if (path.equals(TEST_FILE_PATH)) {
				fileCreated = true;
			} else {
				Assertions.fail("No such file: " + path);
			}
		}
	};

	private final TextFileNicksSource textFileNicksSource = new TextFileNicksSource(
		mockFilesystem,
		TEST_FILE_PATH
	);

	@Test
	public void fetchesNicks() throws ExecutionException, InterruptedException {
		Set<String> nicks = textFileNicksSource.fetchNicks().get();

		assertEquals(
			new HashSet<>(Arrays.asList("A", "B", "C")),
			nicks
		);
	}
}
