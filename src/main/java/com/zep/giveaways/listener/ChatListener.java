package com.zep.giveaways.listener;

import com.zep.giveaways.ZepGiveaways;
import com.zep.giveaways.model.Giveaway;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final ZepGiveaways plugin;

    public ChatListener(ZepGiveaways plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPaperChat(AsyncChatEvent event) {
        Giveaway active = plugin.getGiveawayManager().getActiveGiveaway();
        if (active == null || !active.isActive()) {
            return;
        }

        String rawMsg = PlainTextComponentSerializer.plainText().serialize(event.message()).trim();
        if (rawMsg.equals(active.getKeyword())) {
            Player player = event.getPlayer();
            active.addParticipant(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLegacyChat(AsyncPlayerChatEvent event) {
        Giveaway active = plugin.getGiveawayManager().getActiveGiveaway();
        if (active == null || !active.isActive()) {
            return;
        }

        String rawMsg = event.getMessage().trim();
        if (rawMsg.equals(active.getKeyword())) {
            Player player = event.getPlayer();
            active.addParticipant(player.getUniqueId());
        }
    }
}
