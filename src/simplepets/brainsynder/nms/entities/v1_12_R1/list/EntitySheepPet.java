package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntitySheepPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

public class EntitySheepPet extends AgeableEntityPet implements IEntitySheepPet {
    private static final DataWatcherObject<Byte> COLOR_SHEARED;

    public EntitySheepPet(World world) {
        super(world);
    }
    public EntitySheepPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(COLOR_SHEARED, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("color", getColor().name());
        object.setBoolean("sheared", isSheared());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("color"))
            setColor(DyeColorWrapper.getByName(object.getString("color")));
        if (object.hasKey("sheared"))
            setSheared(object.getBoolean("sheared"));

        super.applyCompound(object);
    }

    public DyeColorWrapper getColor() {
        if (isSheared()) setSheared(false);
        return DyeColorWrapper.getByWoolData(datawatcher.get(COLOR_SHEARED));
    }

    public void setColor(DyeColorWrapper i) {
        if (isSheared()) setSheared(false);
        this.datawatcher.set(COLOR_SHEARED, i.getWoolData());
    }

    public boolean isSheared() {
        byte data = this.datawatcher.get(COLOR_SHEARED);
        return (data & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte data = this.datawatcher.get(COLOR_SHEARED);
        if (flag) {
            this.datawatcher.set(COLOR_SHEARED, (byte) (data | 16));
        } else {
            this.datawatcher.set(COLOR_SHEARED, (byte) (data & -17));
        }
    }

    static {
        COLOR_SHEARED = DataWatcher.a(EntitySheepPet.class, DataWatcherRegistry.a);
    }
}
