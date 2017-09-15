package simplepets.brainsynder.nms.entities.v1_9_R2;

import net.minecraft.server.v1_9_R2.DataWatcher;
import net.minecraft.server.v1_9_R2.DataWatcherObject;
import net.minecraft.server.v1_9_R2.DataWatcherRegistry;
import net.minecraft.server.v1_9_R2.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public abstract class AgeableEntityPet extends EntityPet implements IAgeablePet {
    private static final DataWatcherObject<Boolean> BABY;

    static {
        BABY = DataWatcher.a(AgeableEntityPet.class, DataWatcherRegistry.h);
    }

    public AgeableEntityPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(BABY, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Baby", isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Baby")) {
            setBaby(object.getBoolean("Baby"));
        }
        super.applyCompound(object);
    }

    public boolean isBaby() {
        return this.datawatcher.get(BABY);
    }

    public void setBaby(boolean flag) {
        this.datawatcher.set(BABY, flag);
    }
}
