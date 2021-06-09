package simplepets.brainsynder.versions.v1_17_R1.entity.other;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.entity.misc.IEntityBase;
import simplepets.brainsynder.api.user.PetUser;

public class EntityGhostStand extends EntityArmorStand implements IEntityBase {
    private boolean isSpecial = false;

    public EntityGhostStand(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    public void entityBaseTick() {
        super.entityBaseTick();
        if (isSpecial) {
            setMot(0,0,0);
            setInvisible(true);
        }
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

    @Override
    public EntityType getPetEntityType() {
        return EntityType.ARMOR_STAND;
    }

    public static ArmorStand spawn(Location location, PetUser user) {
        EntityGhostStand stand = new EntityGhostStand(EntityTypes.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle());
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
}
