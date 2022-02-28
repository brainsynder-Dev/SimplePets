package simplepets.brainsynder.nms.utils;

import java.lang.reflect.*;
import java.util.Objects;

/**
 * I cherry-picked the Apache FieldUtils class, so I could get around the hassle of shading it since that was not working :P
 */
public class FieldUtils {
    private static final int ACCESS_TEST = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;
    public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Method IS_SYNTHETIC;

    static {
        Method isSynthetic = null;
        try {
            isSynthetic = Member.class.getMethod("isSynthetic", EMPTY_CLASS_ARRAY);
        } catch (Exception ignored) {
        }
        IS_SYNTHETIC = isSynthetic;
    }

    public static Object readDeclaredField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class cls = target.getClass();
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException("Cannot locate declared field " + cls.getName() + "." + fieldName);
        }
        //already forced access above, don't repeat it here:
        return readField(field, target, false);
    }

    public static Field getDeclaredField(Class cls, String fieldName, boolean forceAccess) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        if (fieldName == null) {
            throw new IllegalArgumentException("The field name must not be null");
        }
        try {
            // only consider the specified class by using getDeclaredField()
            Field field = cls.getDeclaredField(fieldName);
            if (!isAccessible(field)) {
                if (forceAccess) {
                    field.setAccessible(true);
                } else {
                    return null;
                }
            }
            return field;
        } catch (NoSuchFieldException e) {
        }
        return null;
    }

    public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            setAccessibleWorkaround(field);
        }
        return field.get(target);
    }

    static boolean isAccessible(Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !isSynthetic(m);
    }

    static boolean isSynthetic(Member m) {
        if (IS_SYNTHETIC != null) {
            try {
                return ((Boolean) IS_SYNTHETIC.invoke(m, null)).booleanValue();
            } catch (Exception e) {
            }
        }
        return false;
    }

    static void setAccessibleWorkaround(AccessibleObject o) {
        if (o == null || o.isAccessible()) {
            return;
        }
        Member m = (Member) o;
        if (Modifier.isPublic(m.getModifiers())
                && isPackageAccess(m.getDeclaringClass().getModifiers())) {
            try {
                o.setAccessible(true);
            } catch (SecurityException e) {
                // ignore in favor of subsequent IllegalAccessException
            }
        }
    }

    static boolean isPackageAccess(int modifiers) {
        return (modifiers & ACCESS_TEST) == 0;
    }

    public static void writeDeclaredField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(target, "target object must not be null");
        Class<?> cls = target.getClass();
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        Objects.requireNonNull(field, "Cannot locate declared field %s.%s");
        writeField(field, target, value, false);
    }

    public static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(field, "The field must not be null");
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            setAccessibleWorkaround(field);
        }

        field.set(target, value);
    }
}
