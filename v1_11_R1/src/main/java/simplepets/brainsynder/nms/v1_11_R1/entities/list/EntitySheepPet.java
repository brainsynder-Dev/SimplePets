package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

public class EntitySheepPet extends AgeableEntityPet implements IEntitySheepPet {
    private static final DataWatcherObject<Byte> COLOR_SHEARED;
    private boolean rainbow = false;
    private int toggle = 0;

    static {
        COLOR_SHEARED = DataWatcher.a(EntitySheepPet.class, DataWatcherRegistry.a);
    }

    private DyeColorWrapper color = DyeColorWrapper.WHITE;
    private boolean sheared = false;


    public EntitySheepPet(World world) {
        super(world);
    }

    public EntitySheepPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!rainbow)
        object.setString("color", color.name());
        object.setBoolean("Sheared", sheared);
        object.setBoolean("rainbow", rainbow);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
        if (object.hasKey("color"))
            setColor(DyeColorWrapper.valueOf(String.valueOf(object.getString("color"))));
        if (object.hasKey("Sheared"))
            setSheared(object.getBoolean("Sheared"));

        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(COLOR_SHEARED, (byte) 0);
        setSheared(false);
    }

    public DyeColorWrapper getColor() {
        if (sheared) setSheared(false);
        return color;
    }

    public void setColor(DyeColorWrapper i) {
        color = i;
        if (sheared) setSheared(false);
        this.datawatcher.set(COLOR_SHEARED, i.getWoolData());
    }

    public boolean isSheared() {
        return sheared;
    }

    public void setSheared(boolean flag) {
        sheared = flag;
        byte b0 = this.datawatcher.get(COLOR_SHEARED);
        if (flag) {
            this.datawatcher.set(COLOR_SHEARED, (byte) (b0 | 16));
        } else {
            this.datawatcher.set(COLOR_SHEARED, (byte) (b0 & -17));
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
