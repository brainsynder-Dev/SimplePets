package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.json.JsonObject;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityBoggedPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Bogged}
 */
@SupportedVersion(version = ServerVersion.v1_21)
public class EntityBoggedPet extends EntityPetOverride implements IEntityBoggedPet {
    private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(EntityBoggedPet.class, EntityDataSerializers.BOOLEAN);

    public EntityBoggedPet(PetType type, PetUser user) {
        super(EntityType.BOGGED, type, user);
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
    public boolean isSheared() {
        return entityData.get(DATA_SHEARED);
    }

    @Override
    public void setSheared(boolean sheared) {
        entityData.set(DATA_SHEARED, sheared);
    }
}
