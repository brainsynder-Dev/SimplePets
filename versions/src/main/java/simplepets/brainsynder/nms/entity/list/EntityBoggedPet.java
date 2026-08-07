package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import simplepets.brainsynder.api.entity.hostile.IEntityBoggedPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.SHEAR;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Bogged}
 */
@VersionLimit(min = {1, 21, 0})
public class EntityBoggedPet extends EntityPetOverride implements IEntityBoggedPet {
    private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(EntityBoggedPet.class, EntityDataSerializers.BOOLEAN);

    public EntityBoggedPet(PetType type, PetUser user) {
        super(EntitySelector.BOGGED, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("sheared", isSheared());
    }
    
    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_SHEARED, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(SHEAR.namespace(), isSheared());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SHEAR.namespace())) setSheared(object.getBoolean(SHEAR.namespace(), false));
        super.applyCompound(object);
    }

    @Override
    public boolean isSheared() {
        return entityData.get(DATA_SHEARED);
    }

    @Override
    public void setSheared(boolean sheared) {
        entityData.set(DATA_SHEARED, sheared);
    }
}
