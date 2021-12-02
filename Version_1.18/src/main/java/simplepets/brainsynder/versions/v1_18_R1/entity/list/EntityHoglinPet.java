package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityHoglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityHoglin}
 */
public class EntityHoglinPet extends EntityAgeablePet implements IEntityHoglinPet {
    private static EntityDataAccessor<Boolean> IMMUNE_TO_ZOMBIFICATION;
    private static boolean registered = false;

    public EntityHoglinPet(PetType type, PetUser user) {
        super(EntityType.HOGLIN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        if (!registered) {
            IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(EntityHoglinPet.class, EntityDataSerializers.BOOLEAN);
            registered = true;
        }
        this.entityData.define(IMMUNE_TO_ZOMBIFICATION, true); // Makes them not shade by default
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("shaking", isShaking());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("shaking")) setShaking(object.getBoolean("shaking"));
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
