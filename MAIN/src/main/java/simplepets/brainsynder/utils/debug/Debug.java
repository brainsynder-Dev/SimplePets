package simplepets.brainsynder.utils.debug;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.debug.DebugLogger;
import simplepets.brainsynder.files.Config;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Debug implements DebugLogger {
    private final PetCore core;
    private static final Map<String, LinkedList<DebugBuilder>> debugStorage;
    private static final LinkedList<DebugBuilder> debugLog;

    static {
        debugStorage = Maps.newHashMap();
        debugLog = Lists.newLinkedList();
    }

    public Debug (PetCore core) {
        this.core = core;
    }

    @Override
    public void debug(DebugBuilder builder) {
        Runnable runnable = null;
        if (!builder.getMessages().isEmpty()) {
            for (String message : builder.getMessages()) {
                if (message.matches("Player ([a-zA-Z0-9_]+) exists.")) return;
            }
        }

        debugLog.addLast(builder);

        String className = builder.getCallerClassName();
        if (className == null) className = "UNKNOWN";
        LinkedList<DebugBuilder> storage = debugStorage.getOrDefault(className, Lists.newLinkedList());
        storage.addLast(builder);
        debugStorage.put(className, storage);
        if (builder.getLevel().isHidden()) return;

        if (!builder.getLevel().canBypassDisable()) {
            Config configuration = core.getConfiguration();
            if (core.isEnabled() && (configuration != null)) {
                runnable = () -> {
                    if ((configuration != null) && (builder.getLevel().getLevelName() != null)) {
                        if (!ConfigOption.INSTANCE.DEBUG_ENABLED.getValue()) return;
                        if ((builder.getLevel() == DebugLevel.NORMAL) && (!ConfigOption.INSTANCE.DEBUG_NORMAL_LEVEL.getValue())) return;
                        if ((builder.getLevel() == DebugLevel.WARNING) && (!ConfigOption.INSTANCE.DEBUG_WARNING_LEVEL.getValue())) return;
                        if ((builder.getLevel() == DebugLevel.ERROR) && (!ConfigOption.INSTANCE.DEBUG_ERROR_LEVEL.getValue())) return;
                    }
                    if (builder.broadcast())
                        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> {
                            builder.getMessages().forEach(message -> {
                                player.sendMessage(builder.getLevel().getPrefixColor() + "[SimplePets] " + builder.getLevel().getTextColor() + message);
                            });
                        });

                    builder.getMessages().forEach(message -> {
                        Bukkit.getConsoleSender().sendMessage(builder.getLevel().getPrefixColor() + "[SimplePets " + builder.getLevel().getName() + "] " + builder.getLevel().getTextColor() + message);
                    });
                };
            } else {
                runnable = () -> {
                    if (builder.broadcast())
                        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> {
                            builder.getMessages().forEach(message -> {
                                player.sendMessage(builder.getLevel().getPrefixColor() + "[SimplePets] " + builder.getLevel().getTextColor() + message);
                            });
                        });
                    builder.getMessages().forEach(message -> {
                        Bukkit.getConsoleSender().sendMessage(builder.getLevel().getPrefixColor() + "[SimplePets " + builder.getLevel().getName() + "] " + builder.getLevel().getTextColor() + message);
                    });
                };
            }
        }else{
            runnable = () -> {
                if (builder.broadcast())
                    Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> {
                        builder.getMessages().forEach(message -> {
                            player.sendMessage(builder.getLevel().getPrefixColor() + "[SimplePets] " + builder.getLevel().getTextColor() + message);
                        });
                    });
                builder.getMessages().forEach(message -> {
                    Bukkit.getConsoleSender().sendMessage(builder.getLevel().getPrefixColor() + "[SimplePets " + builder.getLevel().getName() + "] " + builder.getLevel().getTextColor() + message);
                });
            };
        }

        if (runnable == null) return;

        if (builder.sync()) {
            core.getScheduler().getImpl().runNextTick(runnable);
        } else {
            runnable.run();
        }

    }

    @Override
    public void debug(String message) {
        debug(message, true);
    }

    @Override
    public void debug(String message, boolean sync) {
        debug(DebugLevel.NORMAL, message, sync);
    }

    @Override
    public void debug(DebugLevel level, String message) {
        debug(level, message, true);
    }

    @Override
    public void debug(DebugLevel level, String message, boolean sync) {
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
                log.set(String.valueOf(builder.timestamp), "["+builder.getLevel()+"] "+ messageBuilder);
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
            data.add("level", builder.getLevel().getName());

            JsonArray messages = new JsonArray();
            builder.getMessages().forEach(messages::add);
            data.add("message", messages);
            if (builder.getCaller() != null) data.add("caller", builder.getCaller());
            information.add(String.valueOf(builder.timestamp), data);
        }
        json.set("detailed_log", information);

        return json;
    }

    @Override
    public LinkedList<DebugBuilder> getDebugLog() {
        return Lists.newLinkedList(debugLog);
    }
}
