package simplepets.brainsynder.debug;

import java.util.LinkedList;

public interface DebugLogger {
    void debug(DebugBuilder builder);

    void debug(String message);
    void debug(String message, boolean sync);

    void debug(DebugLevel level, String message);
    void debug(DebugLevel level, String message, boolean sync);

    LinkedList<DebugBuilder> getDebugLog();
}
