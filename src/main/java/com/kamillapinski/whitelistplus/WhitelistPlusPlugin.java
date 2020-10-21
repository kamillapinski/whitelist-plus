package com.kamillapinski.whitelistplus;

import com.kamillapinski.whitelistplus.commands.KickCommandExecutor;
import com.kamillapinski.whitelistplus.commands.ReloadWhitelistCommandExecutor;
import com.kamillapinski.whitelistplus.config.ConfigurationEntry;
import com.kamillapinski.whitelistplus.dependencies.PluginDependenciesFactory;
import com.kamillapinski.whitelistplus.listeners.PlayerLoginListener;
import com.kamillapinski.whitelistplus.manager.ReloadableWhitelistManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;

public class WhitelistPlusPlugin extends JavaPlugin {
	private final PluginDependenciesFactory pluginDependenciesFactory;

	private ReloadableWhitelistManager reloadableWhitelistManager;

	public WhitelistPlusPlugin(PluginDependenciesFactory pluginDependenciesFactory) {
		this.pluginDependenciesFactory = requireNonNull(pluginDependenciesFactory);
	}

	public WhitelistPlusPlugin() {
		this.pluginDependenciesFactory = PluginDependenciesFactory.productionFactory(this);
	}

	@Override
	public void onEnable() {
		setDefaultConfig();

		if (ConfigurationEntry.WHITELIST_ENABLED.getBoolean(getConfig())) {
			getLogger().info("Whitelist is enabled");

			initWhitelistManager();
			reloadWhitelist();
			registerListeners();
			registerCommands();
		} else {
			getLogger().info("Whitelist is disabled");
		}
	}

	private void registerCommands() {
		ReloadWhitelistCommandExecutor reloadWhitelistExecutor = new ReloadWhitelistCommandExecutor(
			reloadableWhitelistManager,
			getLogger()
		);
		getCommand("whp-reload").setExecutor(reloadWhitelistExecutor);

		KickCommandExecutor kickExecutor = new KickCommandExecutor(
			getServer(),
			getConfig(),
			reloadableWhitelistManager.getWhitelistedChecker(),
			getLogger()
		);
		getCommand("whp-kick").setExecutor(kickExecutor);
	}

	private void initWhitelistManager() {
		reloadableWhitelistManager = pluginDependenciesFactory.reloadableWhitelistManager();
	}

	private void setDefaultConfig() {
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);

		for (ConfigurationEntry configurationEntry : ConfigurationEntry.values()) {
			config.addDefault(configurationEntry.getConfigKey(), configurationEntry.getDefaultValue());
		}

		saveConfig();
	}

	private void registerListeners() {
		PlayerLoginListener.Config listenerConfig = new PlayerLoginListener.Config(
			ConfigurationEntry.NOT_WHITELISTED_MESSAGE.getString(getConfig())
		);

		PlayerLoginListener playerLoginListener = new PlayerLoginListener(
			reloadableWhitelistManager.getWhitelistedChecker(),
			listenerConfig,
			getLogger()
		);

		getServer().getPluginManager().registerEvents(playerLoginListener, this);
		getLogger().log(Level.INFO, "Registered listeners");
	}

	private void reloadWhitelist() {
		try {
			reloadableWhitelistManager.reload().get();
			getLogger().log(Level.INFO, "Loaded whitelist");
		} catch (InterruptedException | ExecutionException e) {
			getLogger().log(Level.SEVERE, e, () -> "Could not load whitelist");
		}
	}
}
