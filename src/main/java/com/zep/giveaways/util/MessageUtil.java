package com.zep.giveaways.util;

import com.zep.giveaways.ZepGiveaways;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Map;

public class MessageUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component parse(String input) {
        if (input == null || input.isEmpty()) {
            return Component.empty();
        }
        return MINI_MESSAGE.deserialize(input);
    }

    public static Component parse(String input, Map<String, String> placeholders) {
        if (input == null || input.isEmpty()) {
            return Component.empty();
        }
        String replaced = input;
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                replaced = replaced.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }
        return parse(replaced);
    }

    public static void sendMessage(CommandSender sender, String messageKey) {
        sendMessage(sender, messageKey, null);
    }

    public static void sendMessage(CommandSender sender, String messageKey, Map<String, String> placeholders) {
        String rawMessage = ZepGiveaways.getInstance().getConfigManager().getLangString("messages." + messageKey);
        if (rawMessage != null && !rawMessage.isEmpty()) {
            String prefix = ZepGiveaways.getInstance().getConfigManager().getPrefix();
            sender.sendMessage(parse(prefix + rawMessage, placeholders));
        }
    }

    public static void sendChatMessage(Player player, String messageKey) {
        sendChatMessage(player, messageKey, null);
    }

    public static void sendChatMessage(Player player, String messageKey, Map<String, String> placeholders) {
        String rawMessage = ZepGiveaways.getInstance().getConfigManager().getLangString("chat." + messageKey);
        if (rawMessage != null && !rawMessage.isEmpty()) {
            String prefix = ZepGiveaways.getInstance().getConfigManager().getPrefix();
            player.sendMessage(parse(prefix + rawMessage, placeholders));
        }
    }

    public static void playSound(Player player, String soundKey) {
        String soundName = ZepGiveaways.getInstance().getConfigManager().getSoundName(soundKey);
        if (soundName != null && !soundName.isEmpty()) {
            try {
                Sound sound = Sound.valueOf(soundName.toUpperCase());
                player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public static void sendTitle(Player player, String titleKey, String subTitleKey, Map<String, String> placeholders) {
        String rawTitle = ZepGiveaways.getInstance().getConfigManager().getLangString("title." + titleKey);
        String rawSubTitle = ZepGiveaways.getInstance().getConfigManager().getLangString("title." + subTitleKey);

        Component titleComp = parse(rawTitle, placeholders);
        Component subTitleComp = parse(rawSubTitle, placeholders);

        int fadeIn = ZepGiveaways.getInstance().getConfigManager().getConfig().getInt("title-timings.fade-in", 10);
        int stay = ZepGiveaways.getInstance().getConfigManager().getConfig().getInt("title-timings.stay", 60);
        int fadeOut = ZepGiveaways.getInstance().getConfigManager().getConfig().getInt("title-timings.fade-out", 20);

        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
        );

        Title titleObj = Title.title(titleComp, subTitleComp, times);
        player.showTitle(titleObj);
    }

    public static void sendPersistentTitle(Player player, String titleKey, String subTitleKey, Map<String, String> placeholders, int staySeconds) {
        String rawTitle = ZepGiveaways.getInstance().getConfigManager().getLangString("title." + titleKey);
        String rawSubTitle = ZepGiveaways.getInstance().getConfigManager().getLangString("title." + subTitleKey);

        Component titleComp = parse(rawTitle, placeholders);
        Component subTitleComp = parse(rawSubTitle, placeholders);

        Title.Times times = Title.Times.times(
                Duration.ofMillis(500L),
                Duration.ofMillis(staySeconds * 1000L),
                Duration.ofMillis(1000L)
        );

        Title titleObj = Title.title(titleComp, subTitleComp, times);
        player.showTitle(titleObj);
    }
}
