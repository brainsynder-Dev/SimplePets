package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityZoglinPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Zoglin}
 */
public class EntityZoglinPet extends EntityPet implements IEntityZoglinPet {
    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(EntityZoglinPet.class, EntityDataSerializers.BOOLEAN);

    public EntityZoglinPet(PetType type, PetUser user) {
        super(EntitySelector.ZOGLIN, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("baby", isBaby());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(BABY, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(PetDataRegistry.BABY.namespace(), isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.BABY.namespace())) setBaby(object.getBoolean(PetDataRegistry.BABY.namespace()));
        super.applyCompound(object);
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
