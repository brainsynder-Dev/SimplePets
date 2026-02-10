package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.SnowGolem}
 */
public class EntitySnowmanPet extends EntityPetOverride implements IEntitySnowmanPet {
    private static final EntityDataAccessor<Byte> PUMPKIN = SynchedEntityData.defineId(EntitySnowmanPet.class, EntityDataSerializers.BYTE);

    public EntitySnowmanPet(PetType type, PetUser user) {
        super(EntityType.SNOW_GOLEM, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("pumpkin", hasPumpkin());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(PUMPKIN, (byte) 0);
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
}
