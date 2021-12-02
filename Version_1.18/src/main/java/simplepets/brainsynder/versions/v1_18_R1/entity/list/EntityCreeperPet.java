package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityCreeperPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityCreeper}
 */
public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet {
    protected static final EntityDataAccessor<Integer> STATE;
    protected static final EntityDataAccessor<Boolean> POWERED;
    protected static final EntityDataAccessor<Boolean> IGNITED;

    public EntityCreeperPet(PetType type, PetUser user) {
        super(EntityType.CREEPER, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(STATE, -1);
        this.entityData.define(POWERED, false);
        this.entityData.define(IGNITED, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("powered", isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("powered")) setPowered(object.getBoolean("powered"));
        super.applyCompound(object);
    }

    @Override
    public boolean isIgnited() {
        return entityData.get(IGNITED);
    }

    @Override
    public void setIgnited(boolean flag) {
        this.entityData.set(IGNITED, flag);
    }

    @Override
    public boolean isPowered() {
        return entityData.get(POWERED);
    }

    @Override
    public void setPowered(boolean flag) {
        this.entityData.set(POWERED, flag);
    }

    static {
        STATE = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.INT);
        POWERED = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.BOOLEAN);
        IGNITED = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.BOOLEAN);
    }
}
