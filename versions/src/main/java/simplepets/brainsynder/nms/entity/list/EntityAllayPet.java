package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import simplepets.brainsynder.api.entity.passive.IEntityAllayPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityFlyablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Allay.DANCING;

/**
 * NMS: {@link net.minecraft.world.entity.animal.allay.Allay }
 */
@VersionLimit(min = {1, 19, 0})
public class EntityAllayPet extends EntityFlyablePet implements IEntityAllayPet {
    private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(EntityAllayPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CAN_DUPLICATE = SynchedEntityData.defineId(EntityAllayPet.class, EntityDataSerializers.BOOLEAN);

    public EntityAllayPet(PetType type, PetUser user) {
        super(EntitySelector.ALLAY, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_DANCING, false);
        dataAccess.define(DATA_CAN_DUPLICATE, false);
    }

    @Override
    public boolean isDancing() {
        return entityData.get(DATA_DANCING);
    }

    @Override
    public void setDancing(boolean dancing) {
        entityData.set(DATA_DANCING, dancing);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("dancing", isDancing());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setBoolean(DANCING.namespace(), isDancing());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(DANCING.namespace())) setDancing(object.getBoolean(DANCING.namespace(), false));
        super.applyCompound(object);
    }
}
