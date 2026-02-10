package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Zombie}
 */
public class EntityZombiePet extends EntityPetOverride implements IEntityZombiePet {
    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> UNKNOWN = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DROWN_CONVERTING = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.BOOLEAN);

    public EntityZombiePet(PetType type, PetUser user) {
        this(EntityType.ZOMBIE, type, user);
    }
    public EntityZombiePet (EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("raised-arms", isArmsRaised());
        data.add("baby", isBabySafe());
        data.add("shaking", isShaking());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(BABY, false);
        dataAccess.define(UNKNOWN, 0);
        dataAccess.define(DROWN_CONVERTING, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("raised_arms", isArmsRaised());
        object.setBoolean("baby", isBabySafe());
        object.setBoolean("shaking", isShaking());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        setArmsRaised(object.getBoolean("raised_arms", false));
        setBabySafe(object.getBoolean("baby", false));
        setShaking(object.getBoolean("shaking", false));
        super.applyCompound(object);
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
    public boolean isBabySafe() {
        return entityData.get(BABY);
    }

    @Override
    public void setBabySafe(boolean flag) {
        entityData.set(BABY, flag);
    }
}
