package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityGoatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGoat}
 */
public class EntityGoatPet extends EntityAgeablePet implements IEntityGoatPet {
    private static final EntityDataAccessor<Boolean> DATA_IS_SCREAMING_GOAT;

    public EntityGoatPet(PetType type, PetUser user) {
        super(EntityType.GOAT, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(DATA_IS_SCREAMING_GOAT, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setBoolean("screaming", isScreaming());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
        super.applyCompound(object);
    }

    @Override
    public boolean isScreaming() {
        return entityData.get(DATA_IS_SCREAMING_GOAT);
    }

    @Override
    public void setScreaming(boolean screaming) {
        entityData.set(DATA_IS_SCREAMING_GOAT, screaming);
    }

    static {
        DATA_IS_SCREAMING_GOAT = SynchedEntityData.defineId(EntityGoatPet.class, EntityDataSerializers.BOOLEAN);
    }
}
