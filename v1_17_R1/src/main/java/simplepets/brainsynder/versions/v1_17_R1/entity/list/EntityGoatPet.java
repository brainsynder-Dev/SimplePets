package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityGoatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGoat}
 */
public class EntityGoatPet extends EntityAgeablePet implements IEntityGoatPet {
    private static final DataWatcherObject<Boolean> DATA_IS_SCREAMING_GOAT;

    public EntityGoatPet(PetType type, PetUser user) {
        super(EntityTypes.MOOSHROOM, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(DATA_IS_SCREAMING_GOAT, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setBoolean("screaming", isScreaming());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
        super.applyCompound(object);
    }

    @Override
    public boolean isScreaming() {
        return datawatcher.get(DATA_IS_SCREAMING_GOAT);
    }

    @Override
    public void setScreaming(boolean screaming) {
        datawatcher.set(DATA_IS_SCREAMING_GOAT, screaming);
    }

    static {
        DATA_IS_SCREAMING_GOAT = DataWatcher.a(EntityGoatPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
