package simplepets.brainsynder.utils.debug;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public enum DebugLevel {
    /**
     * Mostly used when debugging
     *
     *      (Can't be disabled)
     */
    DEBUG(-1, ChatColor.WHITE, ChatColor.AQUA),
    /**
     * Used to send hidden debug messages that can only be seen in the 'debug.json' file
     *
     *      (Can't be disabled)
     */
    HIDDEN(-1, ChatColor.WHITE, ChatColor.GRAY),

    /**
     * Used when facing a critical error
     *
     *      (Can't be disabled)
     */
    CRITICAL("Critical Error", -1, ChatColor.WHITE, ChatColor.RED),

    /**
     * Used for normal messages
     *
     *      (Can be disabled if removed from the Debug level list)
     */
    NORMAL(0, ChatColor.WHITE),

    /**
     * Used for warnings
     *
     *      (Can be disabled if removed from the Debug level list)
     */
    MODERATE(1, ChatColor.YELLOW),

    /**
     * Used if there is a major error
     *
     *      (Can be disabled if removed from the Debug level list)
     */
    ERROR(2, ChatColor.RED),

    /**
     * This one is used if there is an update to the plugin
     *
     *      (Can only be disabled if update checking is off)
     */
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

    public static List<String> getLevels () {
        List<String> list = new ArrayList<>();
        for (DebugLevel level : values()) {
            if ((level != DEBUG) && (level != UPDATE) && (level != HIDDEN)) list.add(level.name());
        }
        return list;
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