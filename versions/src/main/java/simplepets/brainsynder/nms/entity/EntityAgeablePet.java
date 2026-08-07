package simplepets.brainsynder.nms.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.utils.PetDataAccess;

public abstract class EntityAgeablePet extends EntityPetOverride implements IAgeablePet {
    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(EntityAgeablePet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> AGE_LOCKED = SynchedEntityData.defineId(EntityAgeablePet.class, EntityDataSerializers.BOOLEAN);

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
        dataAccess.define(AGE_LOCKED, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!(this instanceof IEntityControllerPet))
            object.setBoolean(PetDataRegistry.BABY.namespace(), isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (!(this instanceof IEntityControllerPet))
            if (object.hasKey(PetDataRegistry.BABY.namespace())) setBaby(object.getBoolean(PetDataRegistry.BABY.namespace()));
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
