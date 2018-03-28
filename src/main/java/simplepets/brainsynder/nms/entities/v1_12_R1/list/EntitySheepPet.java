package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.AgeableEntityPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

@Size(width = 0.9F, length = 1.3F)
public class EntitySheepPet extends AgeableEntityPet implements IEntitySheepPet {
    private static final DataWatcherObject<Byte> COLOR_SHEARED;
    private boolean rainbow = false;
    private int toggle = 0;

    static {
        COLOR_SHEARED = DataWatcher.a(EntitySheepPet.class, DataWatcherRegistry.a);
    }

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
        if (!rainbow)
        object.setString("color", getColor().name());
        object.setBoolean("sheared", isSheared());
        object.setBoolean("rainbow", rainbow);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
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

    @Override
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

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (rainbow) {
            if (toggle == 4) {
                setColor(DyeColorWrapper.getNext(getColor()));
                toggle = 0;
            }
            toggle++;
        }
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}
