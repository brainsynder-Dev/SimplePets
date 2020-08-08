package simplepets.brainsynder.utils;

import org.bukkit.ChatColor;

public enum DebugLevel {
    DEBUG(-1, ChatColor.WHITE, ChatColor.AQUA),
    NORMAL(0, ChatColor.WHITE),
    MODERATE(1, ChatColor.YELLOW),
    ERROR(2, ChatColor.RED);

    private final ChatColor color;
    private final ChatColor prefix;
    private final int level;


    DebugLevel (int level, ChatColor color) {
        this(level, color, ChatColor.GOLD);
    }
    DebugLevel (int level, ChatColor color, ChatColor prefix) {
        this.level = level;
        this.color = color;
        this.prefix = prefix;
    }

    public ChatColor getColor() {
        return color;
    }

    public ChatColor getPrefix() {
        return prefix;
    }

    public int getLevel() {
        return level;
    }
}
