package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityPigPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;

@Size(width = 0.9F, length = 0.9F)
public class EntityPigPet extends AgeableEntityPet implements IEntityPigPet {
    private static final DataWatcherObject<Boolean> SADDLE;

    static {
        SADDLE = DataWatcher.a(EntityPigPet.class, DataWatcherRegistry.h);
    }

    public EntityPigPet(World world) {
        super(world);
    }

    public EntityPigPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        datawatcher.register(SADDLE, false);
        super.registerDatawatchers();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", hasSaddle());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("saddled")) setSaddled(object.getBoolean("saddled"));
        super.applyCompound(object);
    }

    public boolean hasSaddle() {
        return datawatcher.get(SADDLE);
    }

    public void setSaddled(boolean flag) {
        datawatcher.set(SADDLE, flag);
    }
}
