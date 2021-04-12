package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.branch.EntityPiglinAbstractPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPiglin}
 */
public class EntityPiglinPet extends EntityPiglinAbstractPet implements IEntityPiglinPet {
    private static final DataWatcherObject<Boolean> BABY;
    private static final DataWatcherObject<Boolean> CHARGING;
    private static final DataWatcherObject<Boolean> DANCING;

    public EntityPiglinPet(PetType type, PetUser user) {
        super(EntityTypes.PIGLIN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(BABY, false);
        this.datawatcher.register(CHARGING, false);
        this.datawatcher.register(DANCING, false); // Makes them not shake by default
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("baby", isBaby());
        object.setBoolean("charging", isCharging());
        object.setBoolean("dancing", isDancing());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("baby")) setBaby(object.getBoolean("baby"));
        if (object.hasKey("charging")) setCharging(object.getBoolean("charging"));
        if (object.hasKey("dancing")) setDancing(object.getBoolean("dancing"));
        super.applyCompound(object);
    }

    @Override
    public boolean isCharging() {
        return datawatcher.get(CHARGING);
    }

    @Override
    public void setCharging(boolean charging) {
        datawatcher.set(CHARGING, charging);
    }

    @Override
    public boolean isDancing() {
        return datawatcher.get(DANCING);
    }

    @Override
    public void setDancing(boolean dancing) {
        datawatcher.set(DANCING, dancing);
    }

    @Override
    public boolean isBaby() {
        return datawatcher.get(BABY);
    }

    @Override
    public void setBaby(boolean value) {
        datawatcher.set(BABY, value);
    }

    static {
        BABY = DataWatcher.a(EntityPiglinPet.class, DataWatcherWrapper.BOOLEAN);
        CHARGING = DataWatcher.a(EntityPiglinPet.class, DataWatcherWrapper.BOOLEAN);
        DANCING = DataWatcher.a(EntityPiglinPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
