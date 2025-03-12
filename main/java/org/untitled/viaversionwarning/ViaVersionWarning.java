package org.untitled.viaversionwarning;

import com.viaversion.viaversion.api.Via;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ViaVersionWarning extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int version = Via.getAPI().getPlayerVersion(player.getUniqueId());

        if (version < 754) {
            player.sendTitle("§cВаша версія Minecraft застаріла!",
                    "§7(Ви граєте на версії " + version + ")",
                    10, 120, 20);

            Bukkit.getScheduler().runTaskLater(this, () -> {
                player.sendTitle("§7Ми настійно §aрекомендуємо",
                        "§7Перейти на наш сервер з версії §a1.16.5",
                        10, 120, 20);
            }, 140L);

            Bukkit.getScheduler().runTaskLater(this, () -> {
                player.sendTitle("§7Використання старих версій майнкрафт",
                        "§7Може сприяти появі §cбагів§7 та візуальних §cпомилок§7!",
                        10, 120, 20);
            }, 280L);

            Bukkit.getScheduler().runTaskLater(this, () -> animateWestWorld(player), 420L);
        }
    }

    private void animateWestWorld(Player player) {
        String text = "WestWorld";
        String[] letters = text.split("");
        StringBuilder builder = new StringBuilder("§e");

        for (int i = 0; i < letters.length; i++) {
            final int index = i;
            Bukkit.getScheduler().runTaskLater(this, () -> {
                builder.append("§6").append(letters[index]);
                player.sendTitle(builder.toString(), "", 0, 20, 10);

                Bukkit.getScheduler().runTaskLater(this, () -> {
                    builder.setLength(0);
                    builder.append("§f").append(text.substring(0, index + 1));
                    player.sendTitle(builder.toString(), "", 0, 20, 10);
                }, 5L);
            }, i * 7L);
        }
    }
}
