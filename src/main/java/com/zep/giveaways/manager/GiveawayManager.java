package com.zep.giveaways.manager;

import com.zep.giveaways.ZepGiveaways;
import com.zep.giveaways.model.Giveaway;
import com.zep.giveaways.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GiveawayManager {

    private final ZepGiveaways plugin;
    private Giveaway activeGiveaway;
    private BukkitTask timerTask;

    public GiveawayManager(ZepGiveaways plugin) {
        this.plugin = plugin;
    }

    public Giveaway getActiveGiveaway() {
        return activeGiveaway;
    }

    public boolean hasActiveGiveaway() {
        return activeGiveaway != null && activeGiveaway.isActive();
    }

    public int parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return 60;
        }
        timeStr = timeStr.trim().toLowerCase();
        try {
            if (timeStr.endsWith("s")) {
                return Integer.parseInt(timeStr.substring(0, timeStr.length() - 1));
            } else if (timeStr.endsWith("m")) {
                return Integer.parseInt(timeStr.substring(0, timeStr.length() - 1)) * 60;
            } else if (timeStr.endsWith("h")) {
                return Integer.parseInt(timeStr.substring(0, timeStr.length() - 1)) * 3600;
            } else {
                return Integer.parseInt(timeStr) * 60;
            }
        } catch (NumberFormatException e) {
            return 60;
        }
    }

    public String formatTime(int totalSeconds) {
        if (totalSeconds >= 3600) {
            int hours = totalSeconds / 3600;
            int mins = (totalSeconds % 3600) / 60;
            return hours + " saat " + (mins > 0 ? mins + " dakika" : "");
        } else if (totalSeconds >= 60) {
            int mins = totalSeconds / 60;
            int secs = totalSeconds % 60;
            return mins + " dakika " + (secs > 0 ? secs + " saniye" : "");
        } else {
            return totalSeconds + " saniye";
        }
    }

    public boolean startGiveaway(Player player, String keyword, String timeStr) {
        if (hasActiveGiveaway()) {
            MessageUtil.sendMessage(player, "already-active");
            return false;
        }

        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem == null || handItem.getType().isAir()) {
            MessageUtil.sendMessage(player, "no-item-in-hand");
            return false;
        }

        int totalSeconds = parseTime(timeStr);
        activeGiveaway = new Giveaway(keyword, handItem, totalSeconds, player.getUniqueId());

        String formattedTime = formatTime(totalSeconds);
        String itemName = activeGiveaway.getPrizeItemName();

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("keyword", keyword);
        placeholders.put("time", formattedTime);
        placeholders.put("item_name", itemName);

        MessageUtil.sendMessage(player, "started-successfully", placeholders);

        int staySeconds = Math.max(0, totalSeconds - 10);
        for (Player p : Bukkit.getOnlinePlayers()) {
            MessageUtil.playSound(p, "start");
            MessageUtil.sendPersistentTitle(p, "start", "subtitle-start", placeholders, staySeconds);
        }

        startTimerTask();
        return true;
    }

    private void startTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
        }

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (activeGiveaway == null || !activeGiveaway.isActive()) {
                    cancel();
                    return;
                }

                int remaining = activeGiveaway.getRemainingSeconds();

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("keyword", activeGiveaway.getKeyword());
                placeholders.put("time", formatTime(activeGiveaway.getTotalDurationSeconds()));
                placeholders.put("item_name", activeGiveaway.getPrizeItemName());
                placeholders.put("seconds", String.valueOf(remaining));

                if (remaining > 10) {
                } else if (remaining > 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        MessageUtil.playSound(p, "countdown-tick");
                        MessageUtil.sendTitle(p, "countdown", "subtitle-countdown", placeholders);
                    }
                } else {
                    finishGiveaway(false);
                    cancel();
                    return;
                }

                activeGiveaway.decrementRemainingSeconds();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void finishGiveaway(boolean manual) {
        if (!hasActiveGiveaway()) return;

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        activeGiveaway.setActive(false);

        if (manual) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                MessageUtil.sendMessage(p, "finished-manually");
            }
        }

        int minPlayers = plugin.getConfigManager().getConfig().getInt("giveaway.min-players", 1);
        Set<UUID> participants = activeGiveaway.getParticipants();

        if (participants.size() < minPlayers) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                MessageUtil.sendMessage(p, "not-enough-players");
                MessageUtil.playSound(p, "lose");
            }
            activeGiveaway = null;
            return;
        }

        List<UUID> list = new ArrayList<>(participants);
        UUID winnerUuid = list.get(new Random().nextInt(list.size()));
        Player winnerPlayer = Bukkit.getPlayer(winnerUuid);
        String winnerName = winnerPlayer != null ? winnerPlayer.getName() : "Bilinmeyen Oyuncu";

        ItemStack prizeItem = activeGiveaway.getPrizeItem();
        boolean inventoryFull = false;
        
        if (winnerPlayer != null && winnerPlayer.isOnline() && prizeItem != null) {
            HashMap<Integer, ItemStack> leftover = winnerPlayer.getInventory().addItem(prizeItem);
            if (!leftover.isEmpty()) {
                inventoryFull = true;
                MessageUtil.sendChatMessage(winnerPlayer, "winner-inventory-full-self");
                
                Player adminPlayer = Bukkit.getPlayer(activeGiveaway.getStarterUuid());
                if (adminPlayer != null && adminPlayer.isOnline()) {
                    Map<String, String> adminPlaceholders = new HashMap<>();
                    adminPlaceholders.put("winner", winnerName);
                    MessageUtil.sendChatMessage(adminPlayer, "winner-inventory-full-admin", adminPlaceholders);
                }
            }
        }

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("count", String.valueOf(participants.size()));
        placeholders.put("item_name", activeGiveaway.getPrizeItemName());
        placeholders.put("winner", winnerName);

        String announceSelfMsg = plugin.getConfigManager().getLangString("chat.winner-announce-self");
        String announceOthersMsg = plugin.getConfigManager().getLangString("chat.winner-announce-others");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getUniqueId().equals(winnerUuid)) {
                MessageUtil.playSound(p, "win");
                MessageUtil.sendTitle(p, "winner", "subtitle-winner", placeholders);
                p.sendMessage(MessageUtil.parse(announceSelfMsg, placeholders));
            } else {
                MessageUtil.playSound(p, "lose");
                MessageUtil.sendTitle(p, "loser", "subtitle-loser", placeholders);
                p.sendMessage(MessageUtil.parse(announceOthersMsg, placeholders));
            }
        }

        activeGiveaway = null;
    }
}
