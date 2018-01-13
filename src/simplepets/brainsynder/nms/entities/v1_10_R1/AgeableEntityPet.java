package simplepets.brainsynder.nms.entities.v1_10_R1;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class AgeableEntityPet extends EntityPet {

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
        object.setBoolean("baby", isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("baby")) {
            setBaby(object.getBoolean("baby"));
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
