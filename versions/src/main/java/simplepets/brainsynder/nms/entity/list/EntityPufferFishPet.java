package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.PufferState;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityFishPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Pufferfish.SIZE;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Pufferfish}
 */
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet {
    private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(EntityPufferFishPet.class, EntityDataSerializers.INT);

    public EntityPufferFishPet(PetType type, PetUser user) {
        super(EntitySelector.PUFFERFISH, type, user);
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
        object.setEnum(SIZE.namespace(), getPuffState());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SIZE.namespace())) setPuffState(object.getEnum(SIZE.namespace(), PufferState.class, PufferState.SMALL));
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
