package simplepets.brainsynder.utils;

import org.bukkit.ChatColor;

public enum DebugLevel {
    DEBUG(-1, ChatColor.WHITE, ChatColor.AQUA),
    NORMAL(0, ChatColor.WHITE),
    MODERATE(1, ChatColor.YELLOW),
    ERROR(2, ChatColor.RED),
    UPDATE ("Update", -1, ChatColor.GRAY, ChatColor.GREEN);

    private final String string;
    private final ChatColor color;
    private final ChatColor prefix;
    private final int level;

    DebugLevel (int level, ChatColor color) {
        this("Debug", level, color);
    }
    DebugLevel (int level, ChatColor color, ChatColor prefix) {
        this("Debug", level, color, prefix);
    }
    DebugLevel (String string, int level, ChatColor color) {
        this(string, level, color, ChatColor.GOLD);
    }
    DebugLevel (String string, int level, ChatColor color, ChatColor prefix) {
        this.string = string;
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

    public String getString() {
        return string;
    }
}
