package simplepets.brainsynder.utils;

import simplepets.brainsynder.errors.SimplePetsException;

public class Valid {
    public static void isTrue(boolean expression, String message, Object value) {
        if (!expression) {
            throw new SimplePetsException(message + value);
        }
    }

    public static void isTrue(boolean expression, String message, long value) {
        if (!expression) {
            throw new SimplePetsException(message + value);
        }
    }

    public static void isTrue(boolean expression, String message, double value) {
        if (!expression) {
            throw new SimplePetsException(message + value);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new SimplePetsException(message);
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new SimplePetsException("The expression is false");
        }
    }

    public static void notNull(Object object) {
        notNull(object, "The object is null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new SimplePetsException(message);
        }
    }
}
