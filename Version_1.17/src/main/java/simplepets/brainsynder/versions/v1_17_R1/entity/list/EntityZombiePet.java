package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityZombie}
 */
public class EntityZombiePet extends EntityPet implements IEntityZombiePet {
    private static final EntityDataAccessor<Boolean> BABY;
    private static final EntityDataAccessor<Integer> UNKNOWN;
    private static final EntityDataAccessor<Boolean> DROWN_CONVERTING;

    public EntityZombiePet(PetType type, PetUser user) {
        this(EntityType.ZOMBIE, type, user);
    }
    public EntityZombiePet (EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(BABY, false);
        entityData.define(DROWN_CONVERTING, false);
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
        BABY = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.BOOLEAN);
        UNKNOWN = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.INT);
        DROWN_CONVERTING = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.BOOLEAN);
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
        return entityData.get(DROWN_CONVERTING);
    }

    @Override
    public void setShaking(boolean shaking) {
        entityData.set(DROWN_CONVERTING, shaking);
    }

    @Override
    public boolean isBaby() {
        return entityData.get(BABY);
    }

    @Override
    public void setBaby(boolean flag) {
        entityData.set(BABY, flag);
    }
}
