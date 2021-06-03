package simplepets.brainsynder.debug;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class DebugLevel {
    /**
     * Mostly used when debugging
     *
     *      (Can't be disabled)
     */
    public static final DebugLevel DEBUG;
    /**
     * Used to send hidden debug messages that can only be seen in the 'debug.json' file
     *
     *      (Can't be disabled)
     */
    public static final DebugLevel HIDDEN;

    /**
     * Used for normal messages
     *
     *      (Can be disabled if removed from the Debug level list)
     */
    public static final DebugLevel NORMAL;

    /**
     * Used for warnings
     *
     *      (Can be disabled if removed from the Debug level list)
     */
    public static final DebugLevel WARNING;

    /**
     * Used if there is a major error
     *
     *      (Can be disabled if removed from the Debug level list)
     */
    public static final DebugLevel ERROR;

    /**
     * Used when facing a critical error
     *
     *      (Can't be disabled)
     */
    public static final DebugLevel CRITICAL;

    /**
     * This one is used if there is an update to the plugin
     *
     *      (Can only be disabled if update checking is off)
     */
    public static final DebugLevel UPDATE;

    private static final List<DebugLevel> levels;

    static {
        levels = Lists.newArrayList();

        DEBUG = new DebugLevel(ChatColor.WHITE, ChatColor.AQUA).setBypassDisable(true);
        HIDDEN = new DebugLevel(ChatColor.WHITE, ChatColor.GRAY).setBypassDisable(true).setHidden(true);
        NORMAL = new DebugLevel(ChatColor.WHITE).setLevelName("NORMAL");
        WARNING = new DebugLevel("Warning", ChatColor.YELLOW).setLevelName("WARNING");
        ERROR = new DebugLevel("Error", ChatColor.RED).setLevelName("ERROR");
        CRITICAL = new DebugLevel("Critical Error", ChatColor.WHITE, ChatColor.RED).setBypassDisable(true);
        UPDATE = new DebugLevel("Update", ChatColor.GRAY, ChatColor.GREEN).setBypassDisable(true);
    }

    private String levelName = null;
    private final String name;
    private final ChatColor textColor;
    private final ChatColor prefixColor;

    private boolean bypassDisable = false;
    private boolean hidden = false;

    public DebugLevel (ChatColor textColor) {
        this("Debug", textColor);
    }
    public DebugLevel (ChatColor textColor, ChatColor prefixColor) {
        this("Debug", textColor, prefixColor);
    }
    public DebugLevel (String name, ChatColor textColor) {
        this(name, textColor, ChatColor.GOLD);
    }
    public DebugLevel (String name, ChatColor textColor, ChatColor prefixColor) {
        this.name = name;
        this.textColor = textColor;
        this.prefixColor = prefixColor;
        levels.add(this);
    }

    public static List<String> getLevels () {
        List<String> list = new ArrayList<>();
        for (DebugLevel level : levels) {
            if ((level != DEBUG) && (level != UPDATE) && (level != HIDDEN)) list.add(level.getName());
        }
        return list;
    }

    public ChatColor getTextColor() {
        return textColor;
    }

    public ChatColor getPrefixColor() {
        return prefixColor;
    }

    public String getName() {
        return name;
    }

    public DebugLevel setBypassDisable(boolean bypassDisable) {
        this.bypassDisable = bypassDisable;
        return this;
    }

    public DebugLevel setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public DebugLevel setLevelName(String levelName) {
        this.levelName = levelName;
        return this;
    }

    public String getLevelName() {
        return levelName;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean canBypassDisable() {
        return bypassDisable;
    }
}