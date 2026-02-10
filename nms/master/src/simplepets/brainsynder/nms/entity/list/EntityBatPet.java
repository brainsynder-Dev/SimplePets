package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityFlyablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.ambient.Bat}
 */
public class EntityBatPet extends EntityFlyablePet implements IEntityBatPet {
    private static final EntityDataAccessor<Byte> HANGING = SynchedEntityData.defineId(EntityBatPet.class, EntityDataSerializers.BYTE);

    public EntityBatPet(PetType type, PetUser user) {
        super(EntityType.BAT, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(HANGING, (byte) 0);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("hanging", isHanging());
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
}
