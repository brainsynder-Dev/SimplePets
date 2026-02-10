package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.PufferState;
import simplepets.brainsynder.nms.entity.EntityFishPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Pufferfish}
 */
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet {
    private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(EntityPufferFishPet.class, EntityDataSerializers.INT);

    public EntityPufferFishPet(PetType type, PetUser user) {
        super(EntityType.PUFFERFISH, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("state", getPuffState().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(PUFF_STATE, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("size", getPuffState());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("size")) setPuffState(object.getEnum("size", PufferState.class, PufferState.SMALL));
        super.applyCompound(object);
    }

    @Override
    public PufferState getPuffState() {
        return PufferState.getByID(entityData.get(PUFF_STATE));
    }

    @Override
    public void setPuffState(PufferState state) {
        entityData.set(PUFF_STATE, state.ordinal());
    }
}
