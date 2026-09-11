package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.misc.IRaider;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.raid.Raider}
 */
public abstract class EntityRaiderPet extends EntityPetOverride implements IRaider {
    private static final EntityDataAccessor<Boolean> CELEBRATING = SynchedEntityData.defineId(EntityRaiderPet.class, EntityDataSerializers.BOOLEAN);

    public EntityRaiderPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("celebrating", isCelebrating());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(CELEBRATING, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("celebrating", isCelebrating());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("celebrating")) setCelebrating(object.getBoolean("celebrating"));
        super.applyCompound(object);
    }

    @Override
    public boolean isCelebrating() {
        return entityData.get(CELEBRATING);
    }

    @Override
    public void setCelebrating(boolean celebrating) {
        entityData.set(CELEBRATING, celebrating);
    }
}
