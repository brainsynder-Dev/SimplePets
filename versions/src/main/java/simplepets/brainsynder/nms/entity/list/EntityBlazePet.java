package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityFlyablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.BURNING;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Blaze}
 */
public class EntityBlazePet extends EntityFlyablePet implements IEntityBlazePet {
    private static final EntityDataAccessor<Byte> ON_FIRE = SynchedEntityData.defineId(EntityBlazePet.class, EntityDataSerializers.BYTE);

    public EntityBlazePet(PetType type, PetUser user) {
        super(EntitySelector.BLAZE, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("burning", isBurning());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(ON_FIRE, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(BURNING.namespace(), isBurning());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(BURNING.namespace())) setBurning(object.getBoolean(BURNING.namespace()));
        super.applyCompound(object);
    }

    @Override
    public void setBurning(boolean var) {
        byte b1 = this.entityData.get(ON_FIRE);
        if (var) {
            b1 = (byte) (b1 | 1);
        } else {
            b1 &= -2;
        }

        this.entityData.set(ON_FIRE, b1);
    }

    @Override
    public boolean isBurning() {
        return (this.entityData.get(ON_FIRE) & 1) != 0;
    }
}
