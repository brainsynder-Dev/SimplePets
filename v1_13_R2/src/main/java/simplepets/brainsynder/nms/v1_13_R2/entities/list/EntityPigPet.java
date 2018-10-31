package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

@Size(width = 0.9F, length = 0.9F)
public class EntityPigPet extends AgeableEntityPet implements IEntityPigPet {
    private static final DataWatcherObject<Boolean> SADDLE;

    static {
        SADDLE = DataWatcher.a(EntityPigPet.class, DataWatcherRegistry.i);
    }

    public EntityPigPet(World world) {
        super(Types.PIG, world);
    }

    public EntityPigPet(World world, IPet pet) {
        super(Types.PIG, world, pet);
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
