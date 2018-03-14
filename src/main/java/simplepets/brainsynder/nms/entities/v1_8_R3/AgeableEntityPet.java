package simplepets.brainsynder.nms.entities.v1_8_R3;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public abstract class AgeableEntityPet extends EntityPet implements IAgeablePet {
    public AgeableEntityPet(World world, IPet pet) {
        super(world, pet);
        this.datawatcher.a(12, (byte) 0);
    }

    public AgeableEntityPet(World world) {
        super(world);
    }

    public boolean isBaby() {
        if (pet == null) {
            return false;
        }
        return this.datawatcher.getByte(12) == -1;
    }

    public void setBaby(boolean flag) {
        if (pet == null) {
            return;
        }
        if (flag) {
            this.datawatcher.watch(12, (byte) -1);
        } else {
            this.datawatcher.watch(12, (byte) 0);
        }

    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("baby", isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("baby")) {
            setBaby(object.getBoolean("baby"));
        }
        super.applyCompound(object);
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
