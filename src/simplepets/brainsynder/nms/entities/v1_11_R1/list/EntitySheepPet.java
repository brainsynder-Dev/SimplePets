package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntitySheepPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

public class EntitySheepPet extends AgeableEntityPet implements IEntitySheepPet {
    private static final DataWatcherObject<Byte> COLOR_SHEARED;

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
        object.setString("Color", color.name());
        object.setBoolean("Sheared", sheared);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Color"))
            setColor(DyeColorWrapper.valueOf(String.valueOf(object.getString("Color"))));
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
        byte b0 = this.datawatcher.get(COLOR_SHEARED);
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
}
