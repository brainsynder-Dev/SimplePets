package simplepets.brainsynder.utils.debug;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.files.Config;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Debug {
    private static PetCore core;
    private static Map<String, LinkedList<DebugBuilder>> debugStorage;
    private static LinkedList<DebugBuilder> debugLog;

    public static void init(PetCore core) {
        Debug.core = core;
        debugStorage = Maps.newHashMap();
        debugLog = Lists.newLinkedList();
    }

    public static void debug(DebugBuilder builder) {
        Runnable runnable = null;
        debugLog.addLast(builder);

        String className = builder.getCallerClassName();
        if (className == null) className = "UNKNOWN";
        LinkedList<DebugBuilder> storage = debugStorage.getOrDefault(className, Lists.newLinkedList());
        storage.addLast(builder);
        debugStorage.put(className, storage);

        if (builder.getLevel() != DebugLevel.HIDDEN) {
            if (core.isEnabled()) {
                Config configuration = core.getConfiguration();
                runnable = () -> {
                    if ((builder.getLevel() != DebugLevel.DEBUG) && (builder.getLevel() != DebugLevel.CRITICAL) && (builder.getLevel() != DebugLevel.UPDATE) && (configuration != null)) {
                        if (configuration == null) return;
                        if (!configuration.getBoolean("Debug.Enabled")) return;
                        if ((!configuration.getStringList("Debug.Levels").contains(String.valueOf(builder.getLevel().getLevel())))
                                && (!configuration.getStringList("Debug.Levels").contains(builder.getLevel().name())))
                            return;
                    }
                    if (builder.broadcast())
                        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> {
                            builder.getMessages().forEach(message -> {
                                player.sendMessage(builder.getLevel().getPrefix() + "[SimplePets] " + builder.getLevel().getColor() + message);
                            });
                        });

                    builder.getMessages().forEach(message -> {
                        Bukkit.getConsoleSender().sendMessage(builder.getLevel().getPrefix() + "[SimplePets " + builder.getLevel().getString() + "] " + builder.getLevel().getColor() + message);
                    });
                };
            } else {
                runnable = () -> {
                    if (builder.broadcast())
                        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> {
                            builder.getMessages().forEach(message -> {
                                player.sendMessage(builder.getLevel().getPrefix() + "[SimplePets] " + builder.getLevel().getColor() + message);
                            });
                        });
                    builder.getMessages().forEach(message -> {
                        Bukkit.getConsoleSender().sendMessage(builder.getLevel().getPrefix() + "[SimplePets " + builder.getLevel().getString() + "] " + builder.getLevel().getColor() + message);
                    });
                };
            }
        }

        if (runnable == null) return;

        if (builder.sync()) {
            Runnable finalRunnable = runnable;
            new BukkitRunnable() {
                @Override
                public void run() {
                    finalRunnable.run();
                }
            }.runTask(core);
        } else {
            runnable.run();
        }

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

    public static void debugBroadcast(DebugLevel level, String message, boolean sync) {
        debug(DebugBuilder.build().setBroadcast(true).setLevel(level).setMessages(message).setSync(sync));
    }

    public static void debug(DebugLevel level, String message, boolean sync) {
        debug(DebugBuilder.build().setLevel(level).setMessages(message).setSync(sync));
    }

    public static JsonObject fetchLog () {
        JsonObject json = new JsonObject();
        JsonObject information = new JsonObject();
        JsonObject quickLog = new JsonObject();

        debugStorage.forEach((s, logs) -> {
            JsonObject log = new JsonObject();
            LinkedList<DebugBuilder> debugLog = Lists.newLinkedList(logs);

            while (!debugLog.isEmpty()) {
                DebugBuilder builder = debugLog.pollFirst();

                StringBuilder messageBuilder = new StringBuilder().append("[");
                List<String> messages = builder.getMessages();
                boolean cap = (messages.size() > 2);
                int num = 1;
                for (String line : messages) {
                    if ((num > 2) && cap) {
                        messageBuilder.append("...]");
                        break;
                    }

                    messageBuilder.append(line).append(", ");
                    num++;
                }
                log.set(String.valueOf(builder.timestamp), "["+builder.getLevel()+"] "+messageBuilder.toString());
                quickLog.set(s, log);
            }
        });
        json.set("quick_log", quickLog);


        LinkedList<DebugBuilder> debugLog = Lists.newLinkedList(Debug.debugLog);
        while (!debugLog.isEmpty()) {
            JsonObject data = new JsonObject();
            DebugBuilder builder = debugLog.pollFirst();
            Instant instant = Instant.ofEpochMilli ( builder.timestamp );
            ZonedDateTime zdt = ZonedDateTime.ofInstant ( instant , ZoneOffset.UTC );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "yyyy/MM/dd | HH:mm:ss:SSS" );
            String output = formatter.format ( zdt );
            data.add("time/date", output);
            data.add("level", builder.getLevel().name());

            JsonArray messages = new JsonArray();
            builder.getMessages().forEach(messages::add);
            data.add("message", messages);
            if (builder.getCaller() != null) data.add("caller", builder.getCaller());
            information.add(String.valueOf(builder.timestamp), data);
        }
        json.set("detailed_log", information);

        return json;
    }

    public static LinkedList<DebugBuilder> getDebugLog() {
        return Lists.newLinkedList(debugLog);
    }

    public static class DebugValue {
        public long timestamp;
        public List<String> message;
        public DebugLevel level;
        public String callerClass = null;

        public DebugValue(String message, DebugLevel level) {
            this.message = Lists.newArrayList(message);
            this.level = level;
        }

        public DebugValue(List<String> message, DebugLevel level) {
            this.message = message;
            this.level = level;
        }

        public DebugValue setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DebugValue setCallerClass(String callerClass) {
            this.callerClass = callerClass;
            return this;
        }
    }
}
