package simplepets.brainsynder.debug;

import com.google.common.collect.Lists;
import lib.brainsynder.utils.AdvString;

import java.util.List;

public class DebugBuilder {
    /**
     * When was the message send
     */
    public long timestamp;
    /**
     * Will broadcast the message to the players who are OP
     */
    private boolean broadcast = false;
    /**
     * Sync the message to the Main thread
     */
    private boolean sync = false;
    /**
     * The level of debug
     */
    private DebugLevel level;
    private List<String> messages;
    private final String caller;
    private final Class<?> clazz;

    private DebugBuilder () {
        this (null);
    }

    private DebugBuilder (Class<?> clazz) {
        this.clazz = clazz;
        timestamp = System.currentTimeMillis();
        level = DebugLevel.NORMAL;
        messages = Lists.newArrayList();
        caller = getCallerData();
    }

    public DebugBuilder setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
        return this;
    }

    public DebugBuilder setLevel(DebugLevel level) {
        this.level = level;
        return this;
    }

    public DebugBuilder setMessages(String... messages) {
        this.messages = Lists.newArrayList(messages);
        return this;
    }

    public DebugBuilder setSync(boolean sync) {
        this.sync = sync;
        return this;
    }

    public DebugLevel getLevel() {
        return level;
    }

    public List<String> getMessages() {
        return messages;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCaller() {
        return caller;
    }

    private String getCallerData() {
        String callerClassName = null;

        try {
            throw new Exception("Fetching location");
        } catch (Exception exception) {
            for (StackTraceElement element : exception.getStackTrace()) {
                String name = element.getClassName();
                if (name == null) continue;
                if (name.contains("Thread")) continue;
                if (name.toLowerCase().contains("debug")) continue;
                if (!name.contains("simplepets")) continue;
                if ((clazz != null) && (!name.contains(clazz.getName()))) continue;
                String method = element.getMethodName();
                if (method.contains("$")) {
                    method = AdvString.after("$", method);
                }
                method = method.replace("$1", "");

                callerClassName = AdvString.afterLast(".", name).replace("$1", "")+"(method: "+method+"() | line: "+element.getLineNumber()+")";
            }
        }

        return callerClassName;
    }

    public String getCallerClassName () {
        String callerClassName = null;

        try {
            throw new Exception("Fetching location");
        } catch (Exception exception) {
            for (StackTraceElement element : exception.getStackTrace()) {
                String name = element.getClassName();
                if (name == null) continue;
                if (name.contains("Thread")) continue;
                if (name.toLowerCase().contains("debug")) continue;
                if (!name.contains("simplepets")) continue;
                if ((clazz != null) && (!name.contains(clazz.getName()))) continue;

                callerClassName = AdvString.afterLast(".", name).replace("$1", "");
            }
        }

        return callerClassName;
    }

    public boolean broadcast() {
        return broadcast;
    }

    public boolean sync() {
        return sync;
    }


    // STATIC
    public static DebugBuilder build () {
        return build(null);
    }
    public static DebugBuilder build (Class<?> clazz) {
        return new DebugBuilder(clazz);
    }
}
