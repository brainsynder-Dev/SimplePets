package simplepets.brainsynder.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.errors.SimplePetsException;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;

public class Utilities {
    private PetCore instance;
    public Utilities (PetCore instance) {
        this.instance = instance;
    }


    public void setPassenger(Entity entity, Entity passenger) {
        try {
            entity.setPassenger(passenger);
            if (passenger instanceof Player) {
                sendMountPacket((Player) passenger, entity);
            }
        } catch (Exception e) {
            PetCore.get().debug(2, "Could not run method IEntityPet#removePassenger");
            e.printStackTrace();
        }
    }

    public void removePassenger(Entity entity, Entity passenger) {
        try {
            entity.eject();
            if (entity instanceof Player) {
                sendMountPacket((Player) entity, passenger);
            }
        } catch (Exception e) {
            PetCore.get().debug(2, "Could not run method IEntityPet#removePassenger");
            e.printStackTrace();
        }
    }

    public void sendMountPacket(Player player, Entity entity) {
        if (ServerVersion.getVersion() == ServerVersion.v1_8_R3) return;
        Class<?> outMount = ReflectionUtil.getNmsClass("PacketPlayOutMount");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outMount, ReflectionUtil.getNmsClass("Entity"));
        Object packet = ReflectionUtil.initiateClass(constructor, ReflectionUtil.getEntityHandle(entity));
        ReflectionUtil.sendPacket(packet, player);
    }

    public void handlePathfinders(Player player, Entity entity, double speed) {
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.pathfinders." + ReflectionUtil.getVersion() + ".HandlePathfinders");
            if (clazz == null) throw new SimplePetsException("HandlePathfinders not found");
            Constructor<?> con = clazz.getDeclaredConstructor(Player.class, Entity.class, double.class);
            con.newInstance(player, entity, speed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearPathfinders(Entity entity) {
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.pathfinders." + ReflectionUtil.getVersion() + ".ClearPathfinders");
            if (clazz == null) throw new SimplePetsException("ClearPathfinders not found");
            Constructor<?> con = clazz.getDeclaredConstructor(Entity.class);
            con.newInstance(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hidePet(Player player) {
        PetOwner petOwner = PetOwner.getPetOwner(player);
        Class<?> outEntityDestroy = ReflectionUtil.getNmsClass("PacketPlayOutEntityDestroy");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outEntityDestroy, Integer.TYPE);
        Object packet = ReflectionUtil.initiateClass(constructor, petOwner.getPet().getEntity().getEntity().getEntityId());
        ReflectionUtil.sendPacket(packet, player);
    }

    public void showPet(Player player) {
        PetOwner petOwner = PetOwner.getPetOwner(player);
        Class<?> outSpawnEntityLiving = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntityLiving");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outSpawnEntityLiving, ReflectionUtil.getNmsClass("EntityLiving"));
        Object packet = ReflectionUtil.initiateClass(constructor, ReflectionUtil.getEntityHandle(petOwner.getPet().getEntity().getEntity()));
        ReflectionUtil.sendPacket(packet, player);
    }
}
