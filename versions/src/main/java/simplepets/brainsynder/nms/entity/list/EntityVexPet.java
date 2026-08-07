package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityFlyablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.POWERED;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Vex}
 */
public class EntityVexPet extends EntityFlyablePet implements IEntityVexPet {
    protected static final EntityDataAccessor<Byte> VEX_FLAGS = SynchedEntityData.defineId(EntityVexPet.class, EntityDataSerializers.BYTE);

    public EntityVexPet(PetType type, PetUser user) {
        super(EntitySelector.VEX, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("powered", isPowered());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VEX_FLAGS, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(POWERED.namespace(), isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(POWERED.namespace())) setPowered(object.getBoolean(POWERED.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isPowered() {
        return (entityData.get(VEX_FLAGS) & 1) != 0;
    }

    @Override
    public void setPowered(boolean value) {
        byte flag = this.entityData.get(VEX_FLAGS);
        int j;
        if (value) {
            j = flag | 1;
        } else {
            j = flag & ~1;
        }

        this.entityData.set(VEX_FLAGS, (byte) (j & 255));
    }
}
