package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPolarBear}
 */
public class EntityPolarBearPet extends EntityAgeablePet implements IEntityPolarBearPet {
    private static final EntityDataAccessor<Boolean> IS_STANDING;

    public EntityPolarBearPet(PetType type, PetUser user) {
        super(EntityType.POLAR_BEAR, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(IS_STANDING, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("standing", isStanding());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("standing")) setStandingUp(object.getBoolean("standing", false));
        super.applyCompound(object);
    }

    @Override
    public void setStandingUp(boolean flag) {
        this.entityData.set(IS_STANDING, flag);
    }

    @Override
    public boolean isStanding() {
        return this.entityData.get(IS_STANDING);
    }

    static {
        IS_STANDING = SynchedEntityData.defineId(EntityPolarBearPet.class, EntityDataSerializers.BOOLEAN);
    }
}
