package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.MooshroomVariant;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Mooshroom.COLOR;

/**
 * NMS: {@link net.minecraft.world.entity.animal.MushroomCow}
 */
public class EntityMooshroomPet extends EntityAgeablePet implements IEntityMooshroomPet {
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityMooshroomPet.class, EntityDataSerializers.INT);

    public EntityMooshroomPet(PetType type, PetUser user) {
        super(EntitySelector.MOOSHROOM, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("type", getMooshroomType().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(TYPE, MooshroomVariant.RED.name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString(COLOR.namespace(), getMooshroomType().name());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(COLOR.namespace()))
            setMooshroomType(MooshroomVariant.valueOf(object.getString(COLOR.namespace())));
        super.applyCompound(object);
    }

    @Override
    public void setMooshroomType(MooshroomVariant type) {
        entityData.set(TYPE, type.ordinal());
    }

    @Override
    public MooshroomVariant getMooshroomType() {
        try {
            int ordinal = entityData.get(TYPE);
            if (ordinal == 1) return MooshroomVariant.BROWN;
        } catch (Exception ignored) {
            // Randomly the entityData thinks the type is not an Integer...
            // So lets just ignore this as it mostly happens when removing the pet
        }
        return MooshroomVariant.RED;
    }
}
