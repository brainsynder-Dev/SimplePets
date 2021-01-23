package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityPolarBearPet extends EntityAgeablePet implements IEntityPolarBearPet {
    private static final DataWatcherObject<Boolean> IS_STANDING;

    public EntityPolarBearPet(PetType type, PetUser user) {
        super(EntityTypes.POLAR_BEAR, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(IS_STANDING, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("standing", isStanding());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("standing")) setStandingUp(object.getBoolean("standing", false));
        super.applyCompound(object);
    }

    @Override
    public void setStandingUp(boolean flag) {
        this.datawatcher.set(IS_STANDING, flag);
    }

    @Override
    public boolean isStanding() {
        return this.datawatcher.get(IS_STANDING);
    }

    static {
        IS_STANDING = DataWatcher.a(EntityPolarBearPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
