package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPig}
 */
public class EntityPigPet extends EntityAgeablePet implements IEntityPigPet {
    private static final EntityDataAccessor<Boolean> SADDLE;

    public EntityPigPet(PetType type, PetUser user) {
        super(EntityType.PIG, type, user);
        doIndirectAttach = true;
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(SADDLE, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", isSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("saddled")) setSaddled(object.getBoolean("saddled"));
        super.applyCompound(object);
    }

    @Override
    public boolean isSaddled() {
        return entityData.get(SADDLE);
    }

    @Override
    public void setSaddled(boolean flag) {
        entityData.set(SADDLE, flag);
    }

    static {
        SADDLE = SynchedEntityData.defineId(EntityPigPet.class, EntityDataSerializers.BOOLEAN);
    }
}
