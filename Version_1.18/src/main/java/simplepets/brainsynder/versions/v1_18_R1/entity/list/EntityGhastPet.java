package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGhast}
 */
public class EntityGhastPet extends EntityPet implements IEntityGhastPet {
    private static final EntityDataAccessor<Boolean> ATTACKING;

    public EntityGhastPet(PetType type, PetUser user) {
        super(EntityType.GHAST, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(ATTACKING, false);
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


    static {
        ATTACKING = SynchedEntityData.defineId(EntityGhastPet.class, EntityDataSerializers.BOOLEAN);
    }
}
