package simplepets.brainsynder.versions.v1_17_R1.entity;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

public abstract class EntityAgeablePet extends EntityPet implements IAgeablePet {
    private static final DataWatcherObject<Boolean> BABY;

    public EntityAgeablePet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(BABY, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!(this instanceof IEntityControllerPet))
            object.setBoolean("baby", isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (!(this instanceof IEntityControllerPet))
            if (object.hasKey("baby")) setBaby(object.getBoolean("baby"));
        super.applyCompound(object);
    }

    @Override
    public boolean isBaby() {
        return this.datawatcher.get(BABY);
    }

    @Override
    public void setBaby(boolean flag) {
        this.datawatcher.set(BABY, flag);
    }

    static {
        BABY = DataWatcher.a(EntityAgeablePet.class, DataWatcherWrapper.BOOLEAN);
    }
}
