package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityBat}
 */
public class EntityBatPet extends EntityPet implements IEntityBatPet {
    private static final EntityDataAccessor<Byte> HANGING;

    public EntityBatPet(PetType type, PetUser user) {
        super(EntityType.BAT, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(HANGING, (byte) 0);
    }

    @Override
    public boolean isHanging() {
        return (this.entityData.get(HANGING) & 1) != 0;
    }

    @Override
    public void setHanging(boolean flag) {
        byte var2 = this.entityData.get(HANGING);
        if (flag) {
            this.entityData.set(HANGING, (byte) (var2 | 1));
        } else {
            this.entityData.set(HANGING, (byte) (var2 & -2));
        }
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("hanging", isHanging());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("hanging")) setHanging(object.getBoolean("hanging"));
        super.applyCompound(object);
    }


    static {
        HANGING = SynchedEntityData.defineId(EntityBatPet.class, EntityDataSerializers.BYTE);
    }
}
