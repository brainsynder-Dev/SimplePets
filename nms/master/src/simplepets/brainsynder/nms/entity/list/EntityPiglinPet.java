package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.branch.EntityPiglinAbstractPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.piglin.Piglin}
 */
public class EntityPiglinPet extends EntityPiglinAbstractPet implements IEntityPiglinPet {
    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);

    public EntityPiglinPet(PetType type, PetUser user) {
        super(EntityType.PIGLIN, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("baby", isBaby());
        data.add("charging", isCharging());
        data.add("dancing", isDancing());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(BABY, false);
        dataAccess.define(CHARGING, false);
        dataAccess.define(DANCING, false); // Makes them not shake by default - Why is it named dancing then...
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("baby", isBaby());
        object.setBoolean("charging", isCharging());
        object.setBoolean("dancing", isDancing());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("baby")) setBaby(object.getBoolean("baby"));
        if (object.hasKey("charging")) setCharging(object.getBoolean("charging"));
        if (object.hasKey("dancing")) setDancing(object.getBoolean("dancing"));
        super.applyCompound(object);
    }

    @Override
    public boolean isCharging() {
        return entityData.get(CHARGING);
    }

    @Override
    public void setCharging(boolean charging) {
        entityData.set(CHARGING, charging);
    }

    @Override
    public boolean isDancing() {
        return entityData.get(DANCING);
    }

    @Override
    public void setDancing(boolean dancing) {
        entityData.set(DANCING, dancing);
    }

    @Override
    public boolean isBabySafe() {
        return entityData.get(BABY);
    }

    @Override
    public void setBabySafe(boolean value) {
        entityData.set(BABY, value);
    }
}
