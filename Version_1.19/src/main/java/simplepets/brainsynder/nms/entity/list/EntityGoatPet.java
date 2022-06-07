package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.goat.Goat;
import simplepets.brainsynder.api.entity.passive.IEntityGoatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.goat.Goat}
 */
public class EntityGoatPet extends EntityAgeablePet implements IEntityGoatPet {
    private static final EntityDataAccessor<Boolean> DATA_IS_SCREAMING_GOAT;
    public static final EntityDataAccessor<Boolean> DATA_HAS_LEFT_HORN;
    public static final EntityDataAccessor<Boolean> DATA_HAS_RIGHT_HORN;

    public EntityGoatPet(PetType type, PetUser user) {
        super(EntityType.GOAT, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(DATA_IS_SCREAMING_GOAT, false);
        entityData.define(DATA_HAS_LEFT_HORN, true);
        entityData.define(DATA_HAS_RIGHT_HORN, true);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setBoolean("screaming", isScreaming());
        compound.setBoolean("left-horn", hasLeftHorn());
        compound.setBoolean("right-horn", hasRightHorn());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
        if (object.hasKey("left-horn")) setLeftHorn(object.getBoolean("left-horn"));
        if (object.hasKey("right-horn")) setRightHorn(object.getBoolean("right-horn"));
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

    static {
        DATA_IS_SCREAMING_GOAT = SynchedEntityData.defineId(EntityGoatPet.class, EntityDataSerializers.BOOLEAN);
        DATA_HAS_LEFT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
        DATA_HAS_RIGHT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
    }
}
