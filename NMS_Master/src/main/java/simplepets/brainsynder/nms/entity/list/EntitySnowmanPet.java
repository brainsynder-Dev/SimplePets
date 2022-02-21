package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySnowman}
 */
public class EntitySnowmanPet extends EntityPet implements IEntitySnowmanPet {
    private static final EntityDataAccessor<Byte> PUMPKIN;

    public EntitySnowmanPet(PetType type, PetUser user) {
        super(EntityType.SNOW_GOLEM, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(PUMPKIN, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("pumpkin", hasPumpkin());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("pumpkin")) setHasPumpkin(object.getBoolean("pumpkin"));
        super.applyCompound(object);
    }

    @Override
    public boolean hasPumpkin() {
        return (this.entityData.get(PUMPKIN) & 16) != 0;
    }

    @Override
    public void setHasPumpkin(boolean flag) {
        byte b0 = this.entityData.get(PUMPKIN);
        if (flag) {
            this.entityData.set(PUMPKIN, (byte) (b0 | 16));
        } else {
            this.entityData.set(PUMPKIN, (byte) (b0 & -17));
        }
    }

    static {
        PUMPKIN = SynchedEntityData.defineId(EntitySnowmanPet.class, EntityDataSerializers.BYTE);
    }
}
