package simplepets.brainsynder.nms.entity;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.utils.PetDataAccess;

public abstract class EntityAgeablePet extends EntityPetOverride implements IAgeablePet {
    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(EntityAgeablePet.class, EntityDataSerializers.BOOLEAN);

    public EntityAgeablePet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        if (!(this instanceof IEntityControllerPet))
            data.add("baby", isBaby());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(BABY, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!(this instanceof IEntityControllerPet))
            object.setBoolean("baby", isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (!(this instanceof IEntityControllerPet))
            if (object.hasKey("baby")) setBaby(object.getBoolean("baby"));
        super.applyCompound(object);
    }

    @Override
    public boolean isBaby() {
        return isBabySafe();
    }

    @Override
    public void setBaby(boolean flag) {
        setBabySafe(flag);
    }

    @Override
    public boolean isBabySafe() {
        return this.entityData.get(BABY);
    }

    @Override
    public void setBabySafe(boolean flag) {
        this.entityData.set(BABY, flag);
    }
}
