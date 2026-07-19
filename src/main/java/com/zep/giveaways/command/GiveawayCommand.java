package com.zep.giveaways.command;

import com.zep.giveaways.ZepGiveaways;
import com.zep.giveaways.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GiveawayCommand implements CommandExecutor, TabCompleter {

    private final ZepGiveaways plugin;

    public GiveawayCommand(ZepGiveaways plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("cekilis.admin")) {
            MessageUtil.sendMessage(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.sendMessage(sender, "usage-start");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "baslat", "start" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(MessageUtil.parse("<red>Çekiliş başlatmak için oyunda olmalı ve elinizde bir eşya tutmalısınız."));
                    return true;
                }
                if (args.length < 3) {
                    MessageUtil.sendMessage(player, "usage-start");
                    return true;
                }
                String keyword = args[1];
                String timeStr = args[2];
                plugin.getGiveawayManager().startGiveaway(player, keyword, timeStr);
            }
            case "bitir", "finish", "stop" -> plugin.getGiveawayManager().finishGiveaway(true);
            case "reload" -> {
                plugin.getConfigManager().loadConfigs();
                sender.sendMessage(MessageUtil.parse(plugin.getConfigManager().getPrefix() + "<green>Yapılandırma dosyaları başarıyla yenilendi!"));
            }
            default -> MessageUtil.sendMessage(sender, "usage-start");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = List.of("baslat", "bitir", "reload");
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("baslat") || args[0].equalsIgnoreCase("start"))) {
            completions.add("<kelime>");
        } else if (args.length == 3 && (args[0].equalsIgnoreCase("baslat") || args[0].equalsIgnoreCase("start"))) {
            completions.addAll(List.of("30s", "1m", "5m", "10m", "1h"));
        }
        return completions;
    }
}
