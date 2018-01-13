package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_9_R1.AgeableEntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityPigPet extends AgeableEntityPet implements IEntityPigPet {
    private static final DataWatcherObject<Boolean> SADDLE;

    static {
        SADDLE = DataWatcher.a(EntityPigPet.class, DataWatcherRegistry.h);
    }

    public EntityPigPet(net.minecraft.server.v1_9_R1.World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Saddled", hasSaddle());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Saddled"))
            setSaddled(object.getBoolean("Saddled"));
        super.applyCompound(object);
    }

    public boolean hasSaddle() {
        return this.datawatcher.get(SADDLE);
    }

    public void setSaddled(boolean flag) {
        this.datawatcher.set(SADDLE, flag);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(SADDLE, false);
    }
}
