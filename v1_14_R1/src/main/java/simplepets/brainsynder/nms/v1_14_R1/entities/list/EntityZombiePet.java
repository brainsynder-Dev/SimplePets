package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_14_R1.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.EntityPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityZombie}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityZombiePet extends EntityPet implements IEntityZombiePet {
    private static final DataWatcherObject<Boolean> BABY;
    private static final DataWatcherObject<Integer> UNKNOWN;
    private static final DataWatcherObject<Boolean> DROWN_CONVERTING;
    //  Does not work!?! (causes Zombie mobs to be invisible)

    static {
        BABY = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.BOOLEAN);
        UNKNOWN = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.INT);
        DROWN_CONVERTING = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.BOOLEAN);
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
        datawatcher.register(BABY, false);
        datawatcher.register(DROWN_CONVERTING, false);
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
        return super.dR();
    }

    @Override
    public void setArmsRaised(boolean flag) {
        super.q(flag);
    }

    @Override
    public boolean isShaking() {
        return datawatcher.get(DROWN_CONVERTING);
    }

    @Override
    public void setShaking(boolean value) {
        datawatcher.set(DROWN_CONVERTING, value);
    }

    @Override
    public boolean isBaby() {
        return datawatcher.get(BABY);
    }

    @Override
    public void setBaby(boolean flag) {
        datawatcher.set(BABY, flag);
    }
}