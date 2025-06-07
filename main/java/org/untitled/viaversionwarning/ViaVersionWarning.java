package org.untitled.viaversionwarning;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ViaVersionWarning extends JavaPlugin implements Listener {

    private int requiredVersionId;
    private boolean kickPlayer;
    private String requiredVersionName;

    private String serverLang;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void loadSettings() {
        FileConfiguration config = getConfig();
        requiredVersionId = config.getInt("required-version", 754);
        kickPlayer = config.getBoolean("player-kick", false);
        serverLang = config.getString("lang", "en").toLowerCase();

        ProtocolVersion protocol = ProtocolVersion.getProtocol(requiredVersionId);
        requiredVersionName = (protocol != null) ? protocol.getName() : "unknown";
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        int protocolId = Via.getAPI().getPlayerVersion(player.getUniqueId());
        ProtocolVersion playerProtocol = ProtocolVersion.getProtocol(protocolId);
        String playerVersionName = (playerProtocol != null) ? playerProtocol.getName() : "unknown";
        
        String lang = serverLang;

        FileConfiguration config = getConfig();

        if (protocolId < requiredVersionId) {
            sendTitle(player,
                    colorize(getMessage(config, lang, "old-version-title")),
                    colorize(getMessage(config, lang, "old-version-subtitle").replace("%version%", playerVersionName)));

            Bukkit.getScheduler().runTaskLater(this, () -> sendTitle(player,
                    colorize(getMessage(config, lang, "recommend-title")),
                    colorize(getMessage(config, lang, "recommend-subtitle").replace("%required%", requiredVersionName))
            ), 140L);

            Bukkit.getScheduler().runTaskLater(this, () -> sendTitle(player,
                    colorize(getMessage(config, lang, "warning-title")),
                    colorize(getMessage(config, lang, "warning-subtitle"))
            ), 280L);

            if (kickPlayer) {
                Bukkit.getScheduler().runTaskLater(this, () -> player.kickPlayer(
                        colorize(getMessage(config, lang, "kick-message").replace("%required%", requiredVersionName))
                ), 420L);
            }
        }
    }

    private String getMessage(FileConfiguration config, String lang, String key) {
        String path = "lang-messages." + lang + "." + key;
        String msg = config.getString(path);
        if (msg == null || msg.isEmpty()) {
            msg = config.getString("lang-messages.en." + key, "Message missing");
        }
        return msg;
    }

    private void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title != null ? title : "", subtitle != null ? subtitle : "", 10, 120, 20);
    }

    private String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
