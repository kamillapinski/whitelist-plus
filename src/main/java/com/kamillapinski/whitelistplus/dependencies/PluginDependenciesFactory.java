package com.kamillapinski.whitelistplus.dependencies;

import com.kamillapinski.whitelistplus.fs.Filesystem;
import com.kamillapinski.whitelistplus.manager.ReloadableWhitelistManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public interface PluginDependenciesFactory {
	static PluginDependenciesFactory productionFactory(JavaPlugin plugin) {
		Objects.requireNonNull(plugin);

		return new ProductionPluginDependenciesFactory(plugin);
	}

	ReloadableWhitelistManager reloadableWhitelistManager();

	Filesystem filesystem();
}
