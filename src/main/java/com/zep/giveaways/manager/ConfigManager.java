package com.zep.giveaways.manager;

import com.zep.giveaways.ZepGiveaways;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final ZepGiveaways plugin;
    private FileConfiguration config;
    private FileConfiguration langConfig;

    private File configFile;
    private File langFile;

    public ConfigManager(ZepGiveaways plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    public void loadConfigs() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getLangConfig() {
        return langConfig;
    }

    public String getPrefix() {
        return config.getString("prefix", "<blue>ᴢᴇᴘᴄᴇᴋɪʟɪs</blue> ");
    }

    public String getSoundName(String key) {
        return config.getString("sounds." + key, "");
    }

    public String getLangString(String path) {
        return langConfig.getString(path, "");
    }
}
