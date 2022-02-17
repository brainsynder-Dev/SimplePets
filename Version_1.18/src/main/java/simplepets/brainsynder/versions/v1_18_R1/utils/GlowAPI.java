package simplepets.brainsynder.versions.v1_18_R1.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lib.brainsynder.utils.AdvString;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * This class is originally from https://www.spigotmc.org/threads/546391/
 * It has been extensively modified to fix the need of SimplePets
 */
public class GlowAPI {
    private static Map<ChatColor, String> COLOR_KEY;
    private static Map<UUID, ChatColor> COLOR_MAP;
    private static Scoreboard SCOREBOARD;

    public static void init () {
        COLOR_KEY = new HashMap<>();
        COLOR_MAP = new HashMap<>();
        SCOREBOARD = new Scoreboard();
        try {
            for (ChatColor color : ChatColor.values()) {
                if (!color.isColor()) continue;
                ChatFormatting formatting = ChatFormatting.getByName(color.name());
                if (formatting == null) formatting = ChatFormatting.getByCode(color.toString().toCharArray()[1]);
                try {
                    if (formatting == null) formatting = ChatFormatting.valueOf(color.name());
                } catch (Exception ignored) {
                }


                String name = color.name() + AdvString.scramble(AdvString.scramble("12345"));
                COLOR_KEY.put(color, name);
                SCOREBOARD.addPlayerTeam(name).setColor(formatting);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRawColor (Entity entity, ChatColor color) {
        COLOR_MAP.put(entity.getUniqueId(), color);
    }

    public static void setColor (Entity entity, Player player, ChatColor color) {
        try {
            PlayerTeam team = SCOREBOARD.getPlayerTeam(COLOR_KEY.get(color));
            if (entity instanceof Player) {
                SCOREBOARD.addPlayerToTeam(entity.getName(), team);
            } else {
                SCOREBOARD.addPlayerToTeam(String.valueOf(entity.getUniqueId()), team);
            }
            ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true);
            ((CraftPlayer) player).getHandle().connection.send(packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void removeColor (Entity entity, Player player) {
        if (!COLOR_MAP.containsKey(entity.getUniqueId())) return;
        try {
            PlayerTeam team = SCOREBOARD.getPlayerTeam(COLOR_KEY.get(COLOR_MAP.get(entity.getUniqueId())));
            ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createPlayerPacket(team, String.valueOf(entity.getUniqueId()), ClientboundSetPlayerTeamPacket.Action.REMOVE);
            ((CraftPlayer) player).getHandle().connection.send(packet);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public static void setGlowing(Entity entity, Player player, boolean glow) {
//        Disabled till we can fix the issue with Scoreboard Teams & Bungee
//        if (glow) {
//            setColor(entity, player, COLOR_MAP.getOrDefault(entity.getUniqueId(), ChatColor.WHITE));
//        }else{
//            removeColor(entity, player);
//        }

        try {
            net.minecraft.world.entity.Entity gEntity = ((CraftEntity) entity).getHandle();
            SynchedEntityData toCloneDataWatcher = gEntity.getEntityData();
            SynchedEntityData newDataWatcher = new SynchedEntityData(gEntity);

            Int2ObjectMap<SynchedEntityData.DataItem<Byte>> newMap = (Int2ObjectMap<SynchedEntityData.DataItem<Byte>>) readDeclaredField(toCloneDataWatcher, "f", true);

            SynchedEntityData.DataItem<Byte> item = newMap.get(0);
            byte initialBitMask = item.getValue();
            byte bitMaskIndex = (byte) 6;
            if (glow) {
                item.setValue((byte) (initialBitMask | 1 << bitMaskIndex));
            } else {
                item.setValue((byte) (initialBitMask & ~(1 << bitMaskIndex)));
            }
            writeDeclaredField(newDataWatcher, "f", newMap, true);
            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(entity.getEntityId(), newDataWatcher, true);
            ((CraftPlayer) player).getHandle().connection.send(packet);
        } catch (Exception ignored) {
            // I Ignore this in case the server uses ArcLight as that had issues in the past with glowing pets...
        }
    }


    /* THIS IS USED SO I DON'T HAVE TO WORRY ABOUT APACHE COMMONS IMPORT */

    private static Object readDeclaredField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(target, "target");
        final Class<?> cls = target.getClass();
        final Field field = getDeclaredField(cls, fieldName, forceAccess);
        return readField(field, target, false);
    }
    private static Field getDeclaredField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        Objects.requireNonNull(cls, "cls");
        try {
            // only consider the specified class by using getDeclaredField()
            final Field field = cls.getDeclaredField(fieldName);
            if (!isAccessible(field)) {
                if (forceAccess) {
                    field.setAccessible(true);
                } else {
                    return null;
                }
            }
            return field;
        } catch (final NoSuchFieldException e) { // NOPMD
            // ignore
        }
        return null;
    }
    private static Object readField(final Field field, final Object target, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(field, "field");
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            setAccessibleWorkaround(field);
        }
        return field.get(target);
    }
    private static boolean isAccessible(final Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }
    private static boolean setAccessibleWorkaround(final AccessibleObject o) {
        if (o == null || o.isAccessible()) {
            return false;
        }
        final Member m = (Member) o;
        if (!o.isAccessible() && Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
            try {
                o.setAccessible(true);
                return true;
            } catch (final SecurityException e) { // NOPMD
                // ignore in favor of subsequent IllegalAccessException
            }
        }
        return false;
    }
    private static boolean isPackageAccess(final int modifiers) {
        return (modifiers & ACCESS_TEST) == 0;
    }
    private static final int ACCESS_TEST = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

    public static void writeDeclaredField(Object target, String fieldName, Object value, boolean forceAccess)
            throws IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class cls = target.getClass();
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException("Cannot locate declared field " + cls.getName() + "." + fieldName);
        }
        //already forced access above, don't repeat it here:
        writeField(field, target, value, false);
    }
    public static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            setAccessibleWorkaround(field);
        }
        field.set(target, value);
    }
}