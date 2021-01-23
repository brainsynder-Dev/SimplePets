package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityHoglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityHoglinPet extends EntityAgeablePet implements IEntityHoglinPet {
    private static DataWatcherObject<Boolean> IMMUNE_TO_ZOMBIFICATION;
    private static boolean registered = false;

    public EntityHoglinPet(PetType type, PetUser user) {
        super(EntityTypes.HOGLIN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        if (!registered) {
            IMMUNE_TO_ZOMBIFICATION = DataWatcher.a(EntityHoglinPet.class, DataWatcherWrapper.BOOLEAN);
            registered = true;
        }
        this.datawatcher.register(IMMUNE_TO_ZOMBIFICATION, true); // Makes them not shade by default
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
        return !datawatcher.get(IMMUNE_TO_ZOMBIFICATION);
    }

    @Override
    public void setShaking(boolean value) {
        datawatcher.set(IMMUNE_TO_ZOMBIFICATION, !value);
    }
}
