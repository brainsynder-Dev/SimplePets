package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.MooshroomType;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.MushroomCow}
 */
public class EntityMooshroomPet extends EntityAgeablePet implements IEntityMooshroomPet {
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(EntityMooshroomPet.class, EntityDataSerializers.STRING);

    public EntityMooshroomPet(PetType type, PetUser user) {
        super(EntityType.MOOSHROOM, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("type", getMooshroomType().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(TYPE, MooshroomType.RED.name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("type", getMooshroomType().name());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type"))
            setMooshroomType(MooshroomType.valueOf(object.getString("type")));
        super.applyCompound(object);
    }

    @Override
    public void setMooshroomType(MooshroomType type) {
        entityData.set(TYPE, type.name().toLowerCase());
    }

    @Override
    public MooshroomType getMooshroomType() {
        return MooshroomType.valueOf(entityData.get(TYPE).toUpperCase());
    }
}
