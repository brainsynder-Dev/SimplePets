package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntityGoatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Goat.LEFT_HORN;
import static simplepets.brainsynder.api.pet.PetDataRegistry.Goat.RIGHT_HORN;

/**
 * NMS: {@link net.minecraft.world.entity.animal.goat.Goat}
 */
public class EntityGoatPet extends EntityAgeablePet implements IEntityGoatPet {
    private static final EntityDataAccessor<Boolean> DATA_IS_SCREAMING_GOAT = SynchedEntityData.defineId(EntityGoatPet.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_HAS_LEFT_HORN = SynchedEntityData.defineId(EntityGoatPet.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_HAS_RIGHT_HORN = SynchedEntityData.defineId(EntityGoatPet.class, EntityDataSerializers.BOOLEAN);

    public EntityGoatPet(PetType type, PetUser user) {
        super(EntitySelector.GOAT, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("screaming", isScreaming());
        data.add("left-horn", hasLeftHorn());
        data.add("right-horn", hasRightHorn());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_IS_SCREAMING_GOAT, false);
        dataAccess.define(DATA_HAS_LEFT_HORN, true);
        dataAccess.define(DATA_HAS_RIGHT_HORN, true);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setBoolean("screaming", isScreaming());
        compound.setBoolean(LEFT_HORN.namespace(), hasLeftHorn());
        compound.setBoolean(RIGHT_HORN.namespace(), hasRightHorn());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
        if (object.hasKey(LEFT_HORN.namespace())) setLeftHorn(object.getBoolean(LEFT_HORN.namespace()));
        if (object.hasKey(RIGHT_HORN.namespace())) setRightHorn(object.getBoolean(RIGHT_HORN.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isScreaming() {
        return entityData.get(DATA_IS_SCREAMING_GOAT);
    }

    @Override
    public void setScreaming(boolean screaming) {
        entityData.set(DATA_IS_SCREAMING_GOAT, screaming);
    }

    @Override
    public void setLeftHorn(boolean hasHorn) {
        entityData.set(DATA_HAS_LEFT_HORN, hasHorn);
    }

    @Override
    public boolean hasLeftHorn() {
        return entityData.get(DATA_HAS_LEFT_HORN);
    }

    @Override
    public void setRightHorn(boolean hasHorn) {
        entityData.set(DATA_HAS_RIGHT_HORN, hasHorn);
    }

    @Override
    public boolean hasRightHorn() {
        return entityData.get(DATA_HAS_RIGHT_HORN);
    }
}
