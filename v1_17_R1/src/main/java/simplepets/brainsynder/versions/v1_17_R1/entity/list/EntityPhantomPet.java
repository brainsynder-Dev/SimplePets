package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPhantom}
 */
public class EntityPhantomPet extends EntityPet implements IEntitySlimePet {
    private static final EntityDataAccessor<Integer> SIZE;

    public EntityPhantomPet(PetType type, PetUser user) {
        super(EntityType.PHANTOM, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(SIZE, 1);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("size", getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("size")) setSize(object.getInteger("size", 1));
        super.applyCompound(object);
    }

    public int getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(int i) {
        this.entityData.set(SIZE, i);
    }

    static {
        SIZE = SynchedEntityData.defineId(EntityPhantomPet.class, EntityDataSerializers.INT);
    }
}
