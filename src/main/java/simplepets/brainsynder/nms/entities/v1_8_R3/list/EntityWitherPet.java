package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityWitherPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityWitherPet extends EntityPet implements IEntityWitherPet {
    public EntityWitherPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityWitherPet(World world) {
        super(world);
    }

    @Override
    protected String getIdleSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }


    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("shielded", isShielded());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("shielded")) {
            setShielded(object.getBoolean("shielded"));
        }
        super.applyCompound(object);
    }

    @Override
    public boolean isShielded() {
        return false;
    }

    @Override
    public void setShielded(boolean var) {

    }
}
