package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.DataWatcher;
import net.minecraft.server.v1_9_R2.DataWatcherObject;
import net.minecraft.server.v1_9_R2.DataWatcherRegistry;
import net.minecraft.server.v1_9_R2.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityPigPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityPigPet extends AgeableEntityPet implements IEntityPigPet {
    private static final DataWatcherObject<Boolean> SADDLE;

    static {
        SADDLE = DataWatcher.a(EntityPigPet.class, DataWatcherRegistry.h);
    }

    public EntityPigPet(World world, IPet pet) {
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
