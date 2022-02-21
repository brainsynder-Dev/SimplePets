package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityStriderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityStrider}
 */
public class EntityStriderPet extends EntityAgeablePet implements IEntityStriderPet {
    private static EntityDataAccessor<Integer> BOOST_TIME;
    private static EntityDataAccessor<Boolean> COLD;
    private static EntityDataAccessor<Boolean> SADDLED;
    private static boolean registered = false;

    public EntityStriderPet(PetType type, PetUser user) {
        super(EntityType.STRIDER, type, user);
        doIndirectAttach = true;
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("cold", isCold());
        object.setBoolean("saddled", isSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("cold")) setCold(object.getBoolean("cold"));
        if (object.hasKey("saddled")) setSaddled(object.getBoolean("saddled"));
        super.applyCompound(object);
    }

    @Override
    public boolean isSaddled() {
        return entityData.get(SADDLED);
    }

    @Override
    public void setSaddled(boolean saddled) {
        entityData.set(SADDLED, saddled);
    }

    @Override
    public boolean isCold() {
        return entityData.get(COLD);
    }

    @Override
    public void setCold(boolean cold) {
        entityData.set(COLD, cold);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        if (!registered) {
            BOOST_TIME = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.INT);
            COLD = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.BOOLEAN);
            SADDLED = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.BOOLEAN);
            registered = true;
        }
        this.entityData.define(BOOST_TIME, 0);
        this.entityData.define(COLD, false);
        this.entityData.define(SADDLED, false);
    }
}
