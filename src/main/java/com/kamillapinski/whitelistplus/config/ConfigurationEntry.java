package com.kamillapinski.whitelistplus.config;

import org.bukkit.configuration.MemoryConfiguration;

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
		return configuration.getString(getConfigKey());
	}

	public boolean getBoolean(MemoryConfiguration configuration) {
		return configuration.getBoolean(getConfigKey());
	}
}
