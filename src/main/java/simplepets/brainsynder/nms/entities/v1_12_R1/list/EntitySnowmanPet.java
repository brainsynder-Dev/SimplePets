package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;

@Size(width = 0.4F, length = 1.8F)
public class EntitySnowmanPet extends EntityPet implements IEntitySnowmanPet {
    private static final DataWatcherObject<Byte> PUMPKIN;

    static {
        PUMPKIN = DataWatcher.a(EntitySnowmanPet.class, DataWatcherRegistry.a);
    }

    public EntitySnowmanPet(World world) {
        super(world);
    }

    public EntitySnowmanPet(World world, IPet pet) {
        super(world, pet);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(PUMPKIN, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("pumpkin", hasPumpkin());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("pumpkin")) setHasPumpkin(object.getBoolean("pumpkin"));
        super.applyCompound(object);
    }

    public boolean hasPumpkin() {
        return (this.datawatcher.get(PUMPKIN) & 16) != 0;
    }

    public void setHasPumpkin(boolean flag) {
        byte b0 = this.datawatcher.get(PUMPKIN);
        if (flag) {
            this.datawatcher.set(PUMPKIN, (byte) (b0 | 16));
        } else {
            this.datawatcher.set(PUMPKIN, (byte) (b0 & -17));
        }
    }
}
