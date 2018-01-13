package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.AgeableEntityPet;

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
        return datawatcher.get(SADDLE);
    }

    public void setSaddled(boolean flag) {
        try {
            datawatcher.set(SADDLE, flag);
        } catch (Exception e) {
        }
    }

    @Override
    protected void registerDatawatchers() {
        datawatcher.register(SADDLE, false);
        super.registerDatawatchers();
    }
}
