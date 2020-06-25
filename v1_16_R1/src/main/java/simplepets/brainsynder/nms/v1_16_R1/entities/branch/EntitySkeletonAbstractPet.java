package simplepets.brainsynder.nms.v1_16_R1.entities.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R1.*;
import simplepets.brainsynder.api.entity.misc.ISkeletonAbstract;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R1.entities.EntityPet;
import simplepets.brainsynder.nms.v1_16_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntitySkeletonAbstract}
 */
public abstract class EntitySkeletonAbstractPet extends EntityPet implements ISkeletonAbstract {
    private static final DataWatcherObject<Boolean> SWINGING_ARMS;

    static {
        SWINGING_ARMS = DataWatcher.a(EntitySkeletonAbstractPet.class, DataWatcherWrapper.BOOLEAN);
    }


    public EntitySkeletonAbstractPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntitySkeletonAbstractPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SWINGING_ARMS, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("raised", isArmsRaised());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("raised"))
            setArmsRaised(object.getBoolean("raised"));
        super.applyCompound(object);
    }

    @Override
    public boolean isArmsRaised() {
        return datawatcher.get(SWINGING_ARMS);
    }

    @Override
    public void setArmsRaised(boolean flag) {
        datawatcher.set(SWINGING_ARMS, flag);
    }
}