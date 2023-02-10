package net.starly.core.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerUtil {
    public static OfflinePlayer getOfflinePlayer(@NotNull String name) {
        PreCondition.nonNull(name, "name은 null일 수 없습니다.");

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (!(offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline())) return null;
        return offlinePlayer;
    }
}
