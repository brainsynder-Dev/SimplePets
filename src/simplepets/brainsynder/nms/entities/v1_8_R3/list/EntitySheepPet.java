package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntitySheepPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntitySheepPet extends AgeableEntityPet implements IEntitySheepPet {
    public EntitySheepPet(World world, IPet pet) {
        super(world, pet);
        this.datawatcher.a(16, (byte) 0);
    }

    public EntitySheepPet(World world) {
        super(world);
    }


    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("color", getColor().ordinal());
        object.setBoolean("Sheared", isSheared());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("color"))
            setColor(DyeColorWrapper.values()[object.getInteger("color")]);
        if (object.hasKey("Sheared"))
            setSheared(object.getBoolean("Sheared"));

        super.applyCompound(object);
    }

    public void setColor(int i) {
        byte b0 = this.datawatcher.getByte(16);
        byte b = (byte) (b0 & 240 | i & 15);
        this.datawatcher.watch(16, b);
    }

    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByWoolData(getDataWatcher().getByte(16));
    }

    public void setColor(DyeColorWrapper i) {
        if (isSheared()) setSheared(false);
        this.datawatcher.watch(16, i.getWoolData());
    }

    @Override
    public boolean isSheared() {
        byte b0 = this.datawatcher.getByte(16);
        return this.datawatcher.getByte(16) == (b0 | 16);
    }

    public void setSheared(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, (byte) (b0 | 16));
        } else {
            this.datawatcher.watch(16, (byte) (b0 & -17));
        }
    }
}
