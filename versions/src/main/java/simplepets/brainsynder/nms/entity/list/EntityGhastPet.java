package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityFlyablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Ghast.SCREAM;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Ghast}
 */
public class EntityGhastPet extends EntityFlyablePet implements IEntityGhastPet {
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(EntityGhastPet.class, EntityDataSerializers.BOOLEAN);

    public EntityGhastPet(PetType type, PetUser user) {
        super(EntitySelector.GHAST, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("screaming", isScreaming());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(ATTACKING, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(SCREAM.namespace(), isScreaming());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SCREAM.namespace())) setScreaming(object.getBoolean(SCREAM.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isScreaming() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    public void setScreaming(boolean flag) {
        this.entityData.set(ATTACKING, flag);
    }
}
