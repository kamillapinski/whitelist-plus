package com.kamillapinski.whitelistplus.dependencies;

import com.kamillapinski.whitelistplus.config.ConfigurationEntry;
import com.kamillapinski.whitelistplus.fs.StandardFilesystem;
import com.kamillapinski.whitelistplus.fs.Filesystem;
import com.kamillapinski.whitelistplus.manager.NicksSourceWhitelistManager;
import com.kamillapinski.whitelistplus.manager.ReloadableWhitelistManager;
import com.kamillapinski.whitelistplus.nicks.TextFileNicksSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

public class ProductionPluginDependenciesFactory implements PluginDependenciesFactory {
	private final WeakReference<JavaPlugin> plugin;
	private static final Filesystem filesystem = StandardFilesystem.getInstance();

	public ProductionPluginDependenciesFactory(JavaPlugin plugin) {
		Objects.requireNonNull(plugin);

		this.plugin = new WeakReference<>(plugin);
	}

	private JavaPlugin plugin() {
		return plugin.get();
	}

	private Logger logger() {
		return plugin().getLogger();
	}

	private FileConfiguration config() {
		return plugin().getConfig();
	}

	@Override
	public ReloadableWhitelistManager reloadableWhitelistManager() {
		Path whitelistPath = Paths.get(ConfigurationEntry.WHITELIST_FILE_PATH.getString(config()));
		logger().info("Whitelist path: " + whitelistPath);

		TextFileNicksSource nicksSource = new TextFileNicksSource(filesystem(), whitelistPath);
		return new NicksSourceWhitelistManager(nicksSource);
	}

	@Override
	public Filesystem filesystem() {
		return filesystem;
	}
}
