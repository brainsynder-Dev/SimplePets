package simplepets.brainsynder.reflection;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.pet.PetType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {
    public static int getVersionInt() {
        return ServerVersion.getVersion().getIntVersion();
    }

    public static Constructor getConstructor (Class<?> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        }catch (Exception e) {
            return null;
        }
    }

    public static Field getField(Class<?> clazz, String field) {
        if (clazz == null)
            return null;
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
            f.setAccessible(true);
        } catch (Exception e) {
        }
        return f;
    }

    public static void sendPacket(Object packet, Player p) {
        String path = Bukkit.getServer().getClass().getPackage().getName();
        String version = path.substring(path.lastIndexOf(".") + 1);

        try {
            Method getHandle = p.getClass().getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(p);
            Object pConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            Class<?> packetClass = Class.forName("net.minecraft.server." + version + ".Packet");
            Method sendMethod = pConnection.getClass().getMethod("sendPacket", packetClass);
            sendMethod.invoke(pConnection, packet);
        } catch (Exception var9) {
            var9.printStackTrace();
        }
    }

    public static <T> T initiateClass(Constructor<?> constructor, Object... args) {
        try {
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Constructor<?> fillConstructor(Class<?> clazz, Class<?>... values) {
        try {
            return clazz.getDeclaredConstructor(values);
        } catch (Exception e) {
        }
        return null;
    }

    public static Class<?> getNmsClass(String name) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("net.minecraft.server." + getVersion() + '.' + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getPetNMSClass(String name) {
        try {
            return Class.forName("simplepets.brainsynder.nms." + getVersion() + ".entities.list." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static JsonObject getMenuItemsJSON(List<Class<? extends MenuItem>> c, PetType type) {
        JsonObject a = new JsonObject();
        for (Class<? extends MenuItem> cl : c) {
            JsonArray as = new JsonArray();
            MenuItem menuItem = initiateClass(fillConstructor(cl, PetType.class), type);
            try {
                for (Object object : menuItem.getDefaultItems()) {
                    as.add(StorageTagTools.toJsonObject(((ItemBuilder)object).toCompound()));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            a.add(menuItem.getTargetName(), as);
        }
        return a;
    }

    public static Map<String, MenuItem> getMenuItems (List<Class<? extends MenuItem>> c, PetType type) {
        Map<String, MenuItem> map = new HashMap<>();
        for (Class<? extends MenuItem> cl : c)
            map.put(cl.getSimpleName().toLowerCase(), initiateClass(fillConstructor(cl, PetType.class), type));
        return map;
    }

    public static JsonArray getItemsArray (MenuItem menuItem) {
        JsonArray as = new JsonArray();
        try {
            for (Object object : menuItem.getDefaultItems()) {
                as.add(StorageTagTools.toJsonObject(((ItemBuilder)object).toCompound()));
            }
        } catch (NullPointerException ignored) {
        }
        return as;
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    public static Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Class getCBCClass(String className) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + '.' + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            return clazz.getDeclaredMethod(methodName, params);
        } catch (NoSuchMethodException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeMethod(Method method, Object instance, Object... args) {
        try {
            return (T) method.invoke(instance, args);
        } catch (IllegalAccessException var4) {
            return null;
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
