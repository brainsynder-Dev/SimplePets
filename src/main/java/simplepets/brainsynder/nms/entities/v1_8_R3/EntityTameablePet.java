package simplepets.brainsynder.nms.entities.v1_8_R3;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.main.ITameable;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityTameablePet extends AgeableEntityPet implements ITameable {
    public EntityTameablePet(World world) {
        super(world);
    }

    public EntityTameablePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, (byte) 0);
        this.datawatcher.a(17, "");
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Tamed", isTamed());
        object.setBoolean("Sitting", isSitting());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Tamed")) {
            setTamed(object.getBoolean("Tamed"));
        }
        if (object.hasKey("Sitting")) {
            setSitting(object.getBoolean("Sitting"));
        }
        super.applyCompound(object);
    }

    public boolean isTamed() {
        return (this.datawatcher.getByte(16) & 4) != 0;
    }

    public void setTamed(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, (byte) (b0 | 4));
        } else {
            this.datawatcher.watch(16, (byte) (b0 & -5));
        }
    }

    public boolean isSitting() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSitting(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, (byte) (b0 | 1));
        } else {
            this.datawatcher.watch(16, (byte) (b0 & -2));
        }
    }
}
