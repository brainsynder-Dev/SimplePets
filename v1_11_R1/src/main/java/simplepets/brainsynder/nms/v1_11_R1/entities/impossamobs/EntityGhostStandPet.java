package simplepets.brainsynder.nms.v1_11_R1.entities.impossamobs;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.entity.misc.IImpossaPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityGhostStandPet extends EntityArmorStand implements IImpossaPet {
    private boolean isSpecial = false;

    public EntityGhostStandPet(World world) {
        super(world);
    }

    public static ArmorStand spawn(Location location, IPet pet) {
        EntityGhostStandPet stand = new EntityGhostStandPet(((CraftWorld) location.getWorld()).getHandle());
        stand.isSpecial = true;
        stand.setSize(0.0F, 0.0F);
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        stand.setPosition(location.getX(), location.getY(), location.getZ());
        worldServer.addEntity(stand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("invulnerable", true);
        compound.setBoolean("Invisible", true);
        compound.setBoolean("PersistenceRequired", true);
        compound.setBoolean("NoBasePlate", true);
        stand.a(compound);
        return ((ArmorStand) stand.getBukkitEntity());
    }

    @Override
    public void n() {
        super.n();
        if (isSpecial) {
            this.motX = 0;
            this.motY = 0;
            this.motZ = 0;
            setInvisible(true);
        }
    }

    @Override
    public EntityWrapper getEntityType() {
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

    public boolean isSpecial() {return this.isSpecial;}

    public void setSpecial(boolean isSpecial) {this.isSpecial = isSpecial; }
}
