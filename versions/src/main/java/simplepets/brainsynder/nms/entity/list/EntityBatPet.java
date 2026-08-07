package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityFlyablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Bat.HANG;

/**
 * NMS: {@link net.minecraft.world.entity.ambient.Bat}
 */
public class EntityBatPet extends EntityFlyablePet implements IEntityBatPet {
    private static final EntityDataAccessor<Byte> HANGING = SynchedEntityData.defineId(EntityBatPet.class, EntityDataSerializers.BYTE);

    public EntityBatPet(PetType type, PetUser user) {
        super(EntitySelector.BAT, type, user);
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
        object.setBoolean(HANG.namespace(), isHanging());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(HANG.namespace())) setHanging(object.getBoolean(HANG.namespace()));
        super.applyCompound(object);
    }
}
