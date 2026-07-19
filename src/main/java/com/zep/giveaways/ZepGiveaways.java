package com.zep.giveaways;

import com.zep.giveaways.command.GiveawayCommand;
import com.zep.giveaways.listener.ChatListener;
import com.zep.giveaways.manager.ConfigManager;
import com.zep.giveaways.manager.GiveawayManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ZepGiveaways extends JavaPlugin {

    private static ZepGiveaways instance;
    private ConfigManager configManager;
    private GiveawayManager giveawayManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.giveawayManager = new GiveawayManager(this);

        GiveawayCommand commandExecutor = new GiveawayCommand(this);
        PluginCommand cekilisCmd = getCommand("cekilis");
        if (cekilisCmd != null) {
            cekilisCmd.setExecutor(commandExecutor);
            cekilisCmd.setTabCompleter(commandExecutor);
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChatListener(this), this);

        getLogger().info("zepGiveaways v" + getDescription().getVersion() + " (by Redted) kelime katilimli sistemle aktif edildi!");
    }

    @Override
    public void onDisable() {
        if (giveawayManager != null && giveawayManager.hasActiveGiveaway()) {
            giveawayManager.finishGiveaway(true);
        }
        getLogger().info("zepGiveaways devre disi birakildi.");
        instance = null;
    }

    public static ZepGiveaways instance() {
        return instance;
    }

    public static ZepGiveaways getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GiveawayManager getGiveawayManager() {
        return giveawayManager;
    }
}
