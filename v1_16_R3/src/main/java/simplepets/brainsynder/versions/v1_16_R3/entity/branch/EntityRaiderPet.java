package simplepets.brainsynder.versions.v1_16_R3.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.misc.IRaider;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public abstract class EntityRaiderPet extends EntityPet implements IRaider {
    private static final DataWatcherObject<Boolean> CELEBRATING;

    public EntityRaiderPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("celebrating", isCelebrating());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("celebrating")) setCelebrating(object.getBoolean("celebrating"));
        super.applyCompound(object);
    }

    @Override
    public boolean isCelebrating() {
        return datawatcher.get(CELEBRATING);
    }

    @Override
    public void setCelebrating(boolean celebrating) {
        datawatcher.set(CELEBRATING, celebrating);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CELEBRATING, false);
    }

    static {
        CELEBRATING = DataWatcher.a(EntityRaiderPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
