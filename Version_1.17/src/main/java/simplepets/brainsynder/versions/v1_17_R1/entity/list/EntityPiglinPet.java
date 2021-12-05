package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.branch.EntityPiglinAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPiglin}
 */
public class EntityPiglinPet extends EntityPiglinAbstractPet implements IEntityPiglinPet {
    private static final EntityDataAccessor<Boolean> BABY;
    private static final EntityDataAccessor<Boolean> CHARGING;
    private static final EntityDataAccessor<Boolean> DANCING;

    public EntityPiglinPet(PetType type, PetUser user) {
        super(EntityType.PIGLIN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(BABY, false);
        this.entityData.define(CHARGING, false);
        this.entityData.define(DANCING, false); // Makes them not shake by default
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

    static {
        BABY = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
        CHARGING = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
        DANCING = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
    }
}
