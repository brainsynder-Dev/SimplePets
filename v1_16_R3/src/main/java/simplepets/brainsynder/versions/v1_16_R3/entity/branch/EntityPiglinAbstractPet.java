package simplepets.brainsynder.versions.v1_16_R3.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public abstract class EntityPiglinAbstractPet extends EntityPet implements IShaking {
    private static final DataWatcherObject<Boolean> IMMUNE_TO_ZOMBIFICATION;

    public EntityPiglinAbstractPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("shaking", isShaking());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("shaking")) setShaking(object.getBoolean("shaking"));
        super.applyCompound(object);
    }

    @Override
    public boolean isShaking() {
        return datawatcher.get(IMMUNE_TO_ZOMBIFICATION);
    }

    @Override
    public void setShaking(boolean shaking) {
        datawatcher.set(IMMUNE_TO_ZOMBIFICATION, shaking);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(IMMUNE_TO_ZOMBIFICATION, true);
    }

    static {
        IMMUNE_TO_ZOMBIFICATION = DataWatcher.a(EntityPiglinAbstractPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
