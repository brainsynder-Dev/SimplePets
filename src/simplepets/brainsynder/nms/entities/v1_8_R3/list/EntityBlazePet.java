package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityBlazePet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityBlazePet extends EntityPet implements IEntityBlazePet {
    public EntityBlazePet(World world, IPet pet) {
        super(world, pet);
        this.datawatcher.a(16, (byte) 0);
    }

    public EntityBlazePet(World world) {
        super(world);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("burning", isBurning());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("burning")) {
            setBurning(object.getBoolean("burning"));
        }
        super.applyCompound(object);
    }

    @Override
    public boolean isBurning() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    @Override
    public void setBurning(boolean flag) {
        this.datawatcher.watch(16, (byte) (flag ? 1 : 0));
    }

    @Override
    protected String getIdleSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }
}
