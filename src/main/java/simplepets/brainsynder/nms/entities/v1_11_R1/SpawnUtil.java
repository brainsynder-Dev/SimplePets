package simplepets.brainsynder.nms.entities.v1_11_R1;

import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.errors.SimplePetsException;
import simplepets.brainsynder.nms.entities.type.main.IEntityControllerPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.impossamobs.EntityArmorStandPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.impossamobs.EntityGhostStandPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.impossamobs.EntityShulkerPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.list.EntityControllerPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.ISpawner;

public class SpawnUtil implements ISpawner {

    public IEntityPet spawnEntityPet(IPet pet, String className) {
        Location l = pet.getOwner().getLocation();
        return spawn(l, pet, className);
    }

    public IEntityPet spawn(Location l, IPet pet, String className) {
        try {
            World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
            EntityPet customEntity = (EntityPet) ReflectionUtil.getPetNMSClass(className)
                    .getDeclaredConstructor(World.class, IPet.class).newInstance(mcWorld, pet);
            customEntity.setInvisible(false);
            customEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
            mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            if (customEntity instanceof IEntityControllerPet) {
                if (pet.getPetType() == PetType.ARMOR_STAND) {
                    ArmorStand stand = EntityArmorStandPet.spawn(l, ((EntityControllerPet) customEntity));
                    stand.setGravity(false);
                    stand.setArms(true);
                    stand.setCollidable(false);
                    stand.setLeftLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                    stand.setRightLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                    stand.setLeftArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                    stand.setRightArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                    ((IEntityControllerPet) customEntity).setDisplayEntity(stand);
                } else if (pet.getPetType() == PetType.SHULKER) {
                    ArmorStand stand = EntityGhostStandPet.spawn(l, pet);
                    stand.setGravity(false);
                    stand.setCollidable(false);
                    stand.setSmall(true);
                    Shulker shulker = EntityShulkerPet.spawn(l, (EntityControllerPet) customEntity);
                    shulker.setAI(false);
                    shulker.setCollidable(false);
                    stand.setPassenger(shulker);
                    ((IEntityControllerPet) customEntity).setDisplayEntity(stand);
                }
            }
            return customEntity;
        } catch (Exception e) {
            throw new SimplePetsException("Could not summon the " + pet.getPetType().getConfigName() + " Pet", e);
        }
    }

    @Override
    public IEntityPet spawnEntityPet(Location l, IPet pet, String className) {
        return spawn(l, pet, className);
    }
}
