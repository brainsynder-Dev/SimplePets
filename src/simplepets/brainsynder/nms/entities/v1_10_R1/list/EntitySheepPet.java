package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntitySheepPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntitySheepPet extends AgeableEntityPet implements IEntitySheepPet {
    private static final DataWatcherObject<Byte> COLOR_SHEARED;

    static {
        COLOR_SHEARED = DataWatcher.a(EntitySheepPet.class, DataWatcherRegistry.a);
    }

    public EntitySheepPet(World world, IPet pet) {
        super(world, pet);
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

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(COLOR_SHEARED, (byte) 0);
    }

    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByWoolData((byte) ((int) getDataWatcher().get(COLOR_SHEARED)));
    }

    public void setColor(DyeColorWrapper i) {
        if (isSheared()) setSheared(false);
        byte b0 = this.datawatcher.get(COLOR_SHEARED);
        this.datawatcher.set(COLOR_SHEARED, i.getWoolData());
    }

    @Override
    public boolean isSheared() {
        return (this.datawatcher.get(COLOR_SHEARED) & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte b0 = this.datawatcher.get(COLOR_SHEARED);
        if (flag) {
            this.datawatcher.set(COLOR_SHEARED, (byte) (b0 | 16));
        } else {
            this.datawatcher.set(COLOR_SHEARED, (byte) (b0 & -17));
        }
    }
}
