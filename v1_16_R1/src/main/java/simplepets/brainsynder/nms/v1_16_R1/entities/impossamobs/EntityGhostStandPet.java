package simplepets.brainsynder.nms.v1_16_R1.entities.impossamobs;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.entity.misc.IImpossaPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityGhostStandPet extends EntityArmorStand implements IImpossaPet {
    private boolean isSpecial = false;

    public EntityGhostStandPet(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
    }

    private EntityGhostStandPet(EntityTypes<? extends EntityArmorStand> entitytypes, World world, IPet pet) {
        super(entitytypes, world);
    }


    public static ArmorStand spawn(Location location, IPet pet) {
        EntityGhostStandPet stand = new EntityGhostStandPet(EntityTypes.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle(), pet);
        stand.isSpecial = true;
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        stand.setPosition(location.getX(), location.getY(), location.getZ());
        worldServer.addEntity(stand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("invulnerable", true);
        compound.setBoolean("Invisible", true);
        compound.setBoolean("PersistenceRequired", true);
        compound.setBoolean("NoBasePlate", true);
        stand.load(compound);
        return ((ArmorStand) stand.getBukkitEntity());
    }

    /**
     * Runs per-tick
     *
     * Search for: this.world.methodProfiler.a("entityBaseTick");
     * Class: Entity
     */
    @Override
    public void entityBaseTick() {
        super.entityBaseTick();
        if (isSpecial) {
            setMot(0,0,0);
            setInvisible(true);
        }
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.ARMOR_STAND;
    }


    public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
        if (isSpecial)
            return EnumInteractionResult.FAIL;
        return super.a(human, vec3d, enumhand);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (isSpecial)
            return false;
        return super.damageEntity(damagesource, f);
    }
}