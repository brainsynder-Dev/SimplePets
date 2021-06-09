package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityZombie}
 */
public class EntityZombiePet extends EntityPet implements IEntityZombiePet {
    private static final DataWatcherObject<Boolean> BABY;
    private static final DataWatcherObject<Integer> UNKNOWN;
    private static final DataWatcherObject<Boolean> DROWN_CONVERTING;

    public EntityZombiePet(PetType type, PetUser user) {
        this(EntityTypes.ZOMBIE, type, user);
    }
    public EntityZombiePet (EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
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
        object.setBoolean("raised_arms", isArmsRaised());
        object.setBoolean("baby", isBaby());
        object.setBoolean("shaking", isShaking());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        setArmsRaised(object.getBoolean("raised_arms", false));
        setBaby(object.getBoolean("baby", false));
        setShaking(object.getBoolean("shaking", false));
        super.applyCompound(object);
    }

    static {
        BABY = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.BOOLEAN);
        UNKNOWN = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.INT);
        DROWN_CONVERTING = DataWatcher.a(EntityZombiePet.class, DataWatcherWrapper.BOOLEAN);
    }

    @Override
    public void setArmsRaised(boolean flag) {
        super.setAggressive(flag);
    }

    @Override
    public boolean isArmsRaised() {
        return super.isAggressive();
    }

    @Override
    public boolean isShaking() {
        return datawatcher.get(DROWN_CONVERTING);
    }

    @Override
    public void setShaking(boolean shaking) {
        datawatcher.set(DROWN_CONVERTING, shaking);
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
