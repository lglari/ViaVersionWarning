package org.untitled.viaversionwarning;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String githubApiUrl = "https://api.github.com/repos/lglari/ViaVersionWarning/releases/latest";

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(githubApiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }
                    in.close();

                    String response = responseBuilder.toString();

                    String tagName = "unknown";
                    int tagIndex = response.indexOf("\"tag_name\":\"");
                    if (tagIndex != -1) {
                        int start = tagIndex + "\"tag_name\":\"".length();
                        int end = response.indexOf("\"", start);
                        if (end != -1) {
                            tagName = response.substring(start, end);
                        }
                    }

                    String currentVersion = plugin.getDescription().getVersion();
                    plugin.getLogger().info("GitHub latest version: " + tagName);
                    plugin.getLogger().info("Current plugin version: " + currentVersion);

                    if (!tagName.equalsIgnoreCase(currentVersion)) {
                        plugin.getLogger().info("A new version is available! Please update.");
                    } else {
                        plugin.getLogger().info("You are running the latest version (" + currentVersion + ").");
                    }
                } else {
                    plugin.getLogger().warning("Failed to check updates, HTTP response code: " + responseCode);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Exception while checking updates: " + e.getMessage());
            }
        });
    }
}
