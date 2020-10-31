package com.kamillapinski.whitelistplus.config;

import org.bukkit.configuration.MemoryConfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public enum ConfigurationEntry {
	WHITELIST_ENABLED("whitelist.enabled", false),
	WHITELIST_FILE_PATH("whitelist.file-path", "whitelist.txt"),
	NOT_WHITELISTED_MESSAGE("whitelist.not-whitelisted-message", "You are not whitelisted on this server");

	private final String configKey;
	private final Object defaultValue;

	ConfigurationEntry(String configKey, Object defaultValue) {
		this.configKey = requireNonNull(configKey);
		this.defaultValue = defaultValue;
	}

	public String getConfigKey() {
		return configKey;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public String getString(MemoryConfiguration configuration) {
		Objects.requireNonNull(configuration);

		return configuration.getString(getConfigKey());
	}

	public boolean getBoolean(MemoryConfiguration configuration) {
		Objects.requireNonNull(configuration);

		return configuration.getBoolean(getConfigKey());
	}

	public Path getPath(MemoryConfiguration configuration) {
		Objects.requireNonNull(configuration);

		return Paths.get(getString(configuration));
	}
}
