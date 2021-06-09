package simplepets.brainsynder.versions.v1_17_R1.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

public class EntityDonkeyAbstractPet extends EntityHorseAbstractPet implements IChestedAbstractPet {
    private static final DataWatcherObject<Boolean> CHEST;

    public EntityDonkeyAbstractPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("chest", isChested());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        setChested(object.getBoolean("chest", false));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CHEST, Boolean.FALSE);
    }

    @Override
    public boolean isChested() {
        return datawatcher.get(CHEST);
    }

    @Override
    public void setChested(boolean flag) {
        this.datawatcher.set(CHEST, flag);
    }

    static {
        CHEST = DataWatcher.a(EntityDonkeyAbstractPet.class, DataWatcherWrapper.BOOLEAN);
    }
}