package simplepets.brainsynder.reflection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.errors.SimplePetsException;
import simplepets.brainsynder.player.PetOwner;

import java.lang.reflect.Constructor;

public class PerVersion {
    public static void sendHidePacket(Player player) {
        PetOwner petOwner = PetOwner.getPetOwner(player);
        Class<?> outEntityDestroy = ReflectionUtil.getNmsClass("PacketPlayOutEntityDestroy");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outEntityDestroy, Integer.TYPE);
        Object packet = ReflectionUtil.initiateClass(constructor, petOwner.getPet().getEntity().getEntity().getEntityId());
        ReflectionUtil.sendPacket(packet, player);
    }

    public static void sendShowPacket(Player player) {
        PetOwner petOwner = PetOwner.getPetOwner(player);
        Class<?> outSpawnEntityLiving = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntityLiving");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outSpawnEntityLiving, ReflectionUtil.getNmsClass("EntityLiving"));
        Object packet = ReflectionUtil.initiateClass(constructor, ReflectionUtil.getEntityHandle(petOwner.getPet().getEntity().getEntity()));
        ReflectionUtil.sendPacket(packet, player);
    }

    public static void handleMount(Player player, Entity entity) {
        if (ServerVersion.getVersion() == ServerVersion.v1_8_R3) return;
        Class<?> outMount = ReflectionUtil.getNmsClass("PacketPlayOutMount");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outMount, ReflectionUtil.getNmsClass("Entity"));
        Object packet = ReflectionUtil.initiateClass(constructor, ReflectionUtil.getEntityHandle(entity));
        ReflectionUtil.sendPacket(packet, player);
    }

    public static void clearMount(Player player, Entity entity) {
        if (ServerVersion.getVersion() == ServerVersion.v1_8_R3)
            return;
        Class<?> outMount = ReflectionUtil.getNmsClass("PacketPlayOutMount");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outMount, ReflectionUtil.getNmsClass("Entity"));
        Object packet = ReflectionUtil.initiateClass(constructor, ReflectionUtil.getEntityHandle(player));
        ReflectionUtil.sendPacket(packet, player);
    }

    public static void handlePathfinders(Player player, Entity ent, double speed) {
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.pathfinders." + ReflectionUtil.getVersion() + ".HandlePathfinders");
            if (clazz == null) {
                throw new SimplePetsException("HandlePathfinders not found");
            }
            Constructor<?> con = clazz.getDeclaredConstructor(Player.class, Entity.class, double.class);
            con.newInstance(player, ent, speed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearPathfinders(Entity ent) {
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.pathfinders." + ReflectionUtil.getVersion() + ".ClearPathfinders");
            if (clazz == null) {
                throw new SimplePetsException("ClearPathfinders not found");
            }
            Constructor<?> con = clazz.getDeclaredConstructor(Entity.class);
            con.newInstance(ent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
