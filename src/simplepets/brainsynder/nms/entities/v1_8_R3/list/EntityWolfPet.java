package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityWolfPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityWolfPet extends AgeableEntityPet implements IEntityWolfPet {
    public EntityWolfPet(World world) {
        super(world);
    }

    public EntityWolfPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(17, "");
        this.datawatcher.a(16, (byte) 0);
        this.datawatcher.a(18, this.getHealth());
        this.datawatcher.a(19, (byte) 0);
        this.datawatcher.a(20, (byte) 14);
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

    public boolean isTamed() {
        return (this.datawatcher.getByte(16) & 4) != 0;
    }

    public void setTamed(boolean flag) {
        if (this.isAngry() && flag) {
            this.setAngry(false);
        }

        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 4)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -5)));
        }

    }

    public boolean isAngry() {
        return (this.datawatcher.getByte(16) & 2) != 0;
    }

    public void setAngry(boolean flag) {
        if (this.isTamed() && flag) {
            this.setTamed(false);
        }

        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("CollarColor", getColor().ordinal());
        object.setBoolean("Angry", isAngry());
        object.setBoolean("Tamed", isTamed());
        object.setBoolean("Sitting", isSitting());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Sitting"))
            setSitting(object.getBoolean("Sitting"));
        if (object.hasKey("Tamed"))
            setTamed(object.getBoolean("Tamed"));
        if (object.hasKey("Angry")) {
            setAngry(object.getBoolean("Angry"));
        }
        if (object.hasKey("CollarColor"))
            setColor(DyeColorWrapper.values()[object.getInteger("CollarColor")]);
        super.applyCompound(object);
    }

    @Override
    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByDyeData(getDataWatcher().getByte(20));
    }

    public void setColor(DyeColorWrapper dc) {
        if (isTamed()) {
            this.datawatcher.watch(20, (byte) (int) dc.getDyeData());
        }
    }

}
