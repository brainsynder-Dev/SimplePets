package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.golem.SnowGolem}
 */
public class EntitySnowmanPet extends EntityPetOverride implements IEntitySnowmanPet {
    private static final EntityDataAccessor<Byte> PUMPKIN = SynchedEntityData.defineId(EntitySnowmanPet.class, EntityDataSerializers.BYTE);

    public EntitySnowmanPet(PetType type, PetUser user) {
        super(EntitySelector.SNOW_GOLEM, type, user);
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
        object.setBoolean(PetDataRegistry.Snowman.PUMPKIN.namespace(), hasPumpkin());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.Snowman.PUMPKIN.namespace())) setHasPumpkin(object.getBoolean(PetDataRegistry.Snowman.PUMPKIN.namespace()));
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
