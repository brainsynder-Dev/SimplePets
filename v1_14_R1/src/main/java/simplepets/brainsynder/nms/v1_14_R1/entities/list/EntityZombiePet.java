package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityZombie}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityZombiePet extends AgeableEntityPet implements IEntityZombiePet {
    private static final DataWatcherObject<Boolean> ARMS_RAISED;
    private static final DataWatcherObject<Integer> UNKNOWN;
    // private static final DataWatcherObject<Boolean> DROWN_CONVERTING;
    //  Does not work!?! (causes Zombie mobs to be invisible)

    static {
        ARMS_RAISED = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.BOOLEAN);
        UNKNOWN = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.INT);
        //DROWN_CONVERTING = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityZombiePet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityZombiePet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(ARMS_RAISED, false);
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
        return datawatcher.get(ARMS_RAISED);
    }

    @Override
    public void setArmsRaised(boolean flag) {
        datawatcher.set(ARMS_RAISED, flag);
    }
}