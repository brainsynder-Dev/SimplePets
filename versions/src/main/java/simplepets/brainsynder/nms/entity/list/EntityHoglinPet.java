package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityHoglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.SHAKE;

/**
 * NMS: {@link net.minecraft.world.entity.monster.hoglin.Hoglin}
 */
public class EntityHoglinPet extends EntityAgeablePet implements IEntityHoglinPet {
    private static final EntityDataAccessor<Boolean> IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(EntityHoglinPet.class, EntityDataSerializers.BOOLEAN);

    public EntityHoglinPet(PetType type, PetUser user) {
        super(EntitySelector.HOGLIN, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("shaking", isShaking());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(IMMUNE_TO_ZOMBIFICATION, true);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(SHAKE.namespace(), isShaking());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SHAKE.namespace())) setShaking(object.getBoolean(SHAKE.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isShaking() {
        return !entityData.get(IMMUNE_TO_ZOMBIFICATION);
    }

    @Override
    public void setShaking(boolean value) {
        entityData.set(IMMUNE_TO_ZOMBIFICATION, !value);
    }
}
