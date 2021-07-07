package simplepets.brainsynder.versions.v1_17_R1.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.misc.IRaider;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;

public abstract class EntityRaiderPet extends EntityPet implements IRaider {
    private static final EntityDataAccessor<Boolean> CELEBRATING;

    public EntityRaiderPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("celebrating", isCelebrating());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("celebrating")) setCelebrating(object.getBoolean("celebrating"));
        super.applyCompound(object);
    }

    @Override
    public boolean isCelebrating() {
        return entityData.get(CELEBRATING);
    }

    @Override
    public void setCelebrating(boolean celebrating) {
        entityData.set(CELEBRATING, celebrating);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(CELEBRATING, false);
    }

    static {
        CELEBRATING = SynchedEntityData.defineId(EntityRaiderPet.class, EntityDataSerializers.BOOLEAN);
    }
}
