package simplepets.brainsynder.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.files.Config;

public class Debug {
    private static PetCore core;

    public static void init (PetCore core) {
        Debug.core = core;
    }

    public static void debug(String message) {
        debug(message, true);
    }

    public static void debug(String message, boolean sync) {
        debug(DebugLevel.NORMAL, message, sync);
    }

    public static void debug(DebugLevel level, String message) {
        debug(level, message, true);
    }

    public static void debug(DebugLevel level, String message, boolean sync) {
        if (!core.isEnabled()) return;
        Config configuration = core.getConfiguration();
        Runnable runnable = () -> {
            if ((level != DebugLevel.DEBUG) && (level != DebugLevel.UPDATE) && (configuration != null)) {
                if (configuration == null) return;
                if (!configuration.getBoolean("Debug.Enabled")) return;
                if (!configuration.getStringList("Debug.Levels").contains(String.valueOf(level.getLevel()))) return;
                if (!configuration.getStringList("Debug.Levels").contains(level.name())) return;
            }
            Bukkit.getConsoleSender().sendMessage(level.getPrefix() + "[SimplePets " + level.getString() + "] " + level.getColor() + message);
        };

        if (sync) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTask(core);
        } else {
            runnable.run();
        }
    }
}
