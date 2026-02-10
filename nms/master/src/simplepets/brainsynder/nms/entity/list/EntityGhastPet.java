package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Ghast}
 */
public class EntityGhastPet extends EntityPetOverride implements IEntityGhastPet {
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(EntityGhastPet.class, EntityDataSerializers.BOOLEAN);

    // TODO: Need to figure out why the Ghast pet wont work properly if given the Flight MoveController
    public EntityGhastPet(PetType type, PetUser user) {
        super(EntityType.GHAST, type, user);
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
        object.setBoolean("screaming", isScreaming());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
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
