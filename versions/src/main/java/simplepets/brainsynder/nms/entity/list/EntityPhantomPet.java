package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityPhantomPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityFlyablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Phantom}
 */
public class EntityPhantomPet extends EntityFlyablePet implements IEntityPhantomPet {
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(EntityPhantomPet.class, EntityDataSerializers.INT);

    public EntityPhantomPet(PetType type, PetUser user) {
        super(EntitySelector.PHANTOM, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("size", getSize());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(SIZE, 1);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger(PetDataRegistry.SIZE.namespace(), getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.SIZE.namespace())) setSize(object.getInteger(PetDataRegistry.SIZE.namespace(), 1));
        super.applyCompound(object);
    }

    public int getSize() {
        return this.entityData.get(SIZE);
    }

    public void setSize(int i) {
        this.entityData.set(SIZE, i);
    }
}
