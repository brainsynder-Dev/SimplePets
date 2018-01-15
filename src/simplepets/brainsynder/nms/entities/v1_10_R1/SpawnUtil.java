package simplepets.brainsynder.nms.entities.v1_10_R1;

import net.minecraft.server.v1_10_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.ISpawner;

import java.lang.reflect.InvocationTargetException;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class SpawnUtil implements ISpawner {
    public IEntityPet spawnEntityPet(IPet pet, String className) {
        try {
            Location l = pet.getOwner().getLocation();
            World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
            EntityPet customEntity = (EntityPet) ReflectionUtil.getPetNMSClass(className).getDeclaredConstructor(World.class, IPet.class).newInstance(mcWorld, pet);
            customEntity.setInvisible(false);
            customEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
            mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            return customEntity;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IEntityPet spawnEntityPet(Location l, IPet pet, String className) {
        try {
            World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
            EntityPet customEntity = (EntityPet) ReflectionUtil.getPetNMSClass(className).getDeclaredConstructor(World.class, IPet.class).newInstance(mcWorld, pet);
            customEntity.setInvisible(false);
            customEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
            mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            return customEntity;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}