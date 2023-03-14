package com.kamillapinski.whitelistplus.listeners;

import com.kamillapinski.whitelistplus.access.PlayerWhitelistedChecker;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class PlayerLoginListener implements Listener {
    private final PlayerWhitelistedChecker playerWhitelistedChecker;
    private final Config config;
    private final Logger logger;

    public PlayerLoginListener(PlayerWhitelistedChecker playerWhitelistedChecker, Config config, Logger logger) {
        this.playerWhitelistedChecker = requireNonNull(playerWhitelistedChecker);
        this.config = requireNonNull(config);
        this.logger = requireNonNull(logger);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent loginEvent) {
        Player player = loginEvent.getPlayer();

        if (!playerWhitelistedChecker.isWhitelisted(player)) {
            loginEvent.disallow(
                PlayerLoginEvent.Result.KICK_WHITELIST,
                Component.text(config.notWhitelistedMessage())
            );

            logger.info("Kicked player " + player.getName());
        } else {
            logger.info("Allowed player " + player.getName());
        }
    }

    public static final class Config {
        private final String notWhitelistedMessage;

        public Config(String notWhitelistedMessage) {
            this.notWhitelistedMessage = requireNonNull(notWhitelistedMessage);
        }

        public String notWhitelistedMessage() {
            return notWhitelistedMessage;
        }
    }
}
