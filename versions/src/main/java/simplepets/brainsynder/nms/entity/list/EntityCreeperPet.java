package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.hostile.IEntityCreeperPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Creeper}
 */
public class EntityCreeperPet extends EntityPetOverride implements IEntityCreeperPet {
    protected static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.BOOLEAN);

    public EntityCreeperPet(PetType type, PetUser user) {
        super(EntitySelector.CREEPER, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("powered", isPowered());
        data.add("ignited", isIgnited());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(STATE, -1);
        dataAccess.define(POWERED, false);
        dataAccess.define(IGNITED, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(PetDataRegistry.POWERED.namespace(), isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.POWERED.namespace())) setPowered(object.getBoolean(PetDataRegistry.POWERED.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isIgnited() {
        return entityData.get(IGNITED);
    }

    @Override
    public void setIgnited(boolean flag) {
        this.entityData.set(IGNITED, flag);
    }

    @Override
    public boolean isPowered() {
        return entityData.get(POWERED);
    }

    @Override
    public void setPowered(boolean flag) {
        this.entityData.set(POWERED, flag);
    }
}
