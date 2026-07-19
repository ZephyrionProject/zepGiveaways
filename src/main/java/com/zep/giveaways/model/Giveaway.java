package com.zep.giveaways.model;

import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Giveaway {

    private final String keyword;
    private final ItemStack prizeItem;
    private final UUID starterUuid;
    private final Set<UUID> participants;
    private final int totalDurationSeconds;
    private int remainingSeconds;
    private boolean active;

    public Giveaway(String keyword, ItemStack prizeItem, int totalDurationSeconds, UUID starterUuid) {
        this.keyword = keyword;
        this.prizeItem = prizeItem != null ? prizeItem.clone() : null;
        this.starterUuid = starterUuid;
        this.participants = java.util.concurrent.ConcurrentHashMap.newKeySet();
        this.totalDurationSeconds = totalDurationSeconds;
        this.remainingSeconds = totalDurationSeconds;
        this.active = true;
    }

    public UUID getStarterUuid() {
        return starterUuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public ItemStack getPrizeItem() {
        return prizeItem != null ? prizeItem.clone() : null;
    }

    public String getPrizeItemName() {
        if (prizeItem == null) return "Ödül Yok";
        if (prizeItem.hasItemMeta() && prizeItem.getItemMeta().hasDisplayName()) {
            String rawName = prizeItem.getItemMeta().getDisplayName();
            return MiniMessage.miniMessage().serialize(LegacyComponentSerializer.legacySection().deserialize(rawName));
        }
        String typeName = prizeItem.getType().name().replace("_", " ").toLowerCase();
        return prizeItem.getAmount() + "x " + typeName;
    }

    public String getPrizeName() {
        return getPrizeItemName();
    }

    public Set<UUID> getParticipants() {
        return participants;
    }

    public boolean addParticipant(UUID uuid) {
        return participants.add(uuid);
    }

    public boolean removeParticipant(UUID uuid) {
        return participants.remove(uuid);
    }

    public boolean isParticipant(UUID uuid) {
        return participants.contains(uuid);
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public int getTotalDurationSeconds() {
        return totalDurationSeconds;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public void decrementRemainingSeconds() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
