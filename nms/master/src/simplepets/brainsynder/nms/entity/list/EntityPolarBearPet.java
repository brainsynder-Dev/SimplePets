package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.PolarBear}
 */
public class EntityPolarBearPet extends EntityAgeablePet implements IEntityPolarBearPet {
    private static final EntityDataAccessor<Boolean> IS_STANDING = SynchedEntityData.defineId(EntityPolarBearPet.class, EntityDataSerializers.BOOLEAN);

    public EntityPolarBearPet(PetType type, PetUser user) {
        super(EntityType.POLAR_BEAR, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("standing", isStanding());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(IS_STANDING, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("standing", isStanding());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("standing")) setStandingUp(object.getBoolean("standing", false));
        super.applyCompound(object);
    }

    @Override
    public void setStandingUp(boolean flag) {
        this.entityData.set(IS_STANDING, flag);
    }

    @Override
    public boolean isStanding() {
        return this.entityData.get(IS_STANDING);
    }
}
