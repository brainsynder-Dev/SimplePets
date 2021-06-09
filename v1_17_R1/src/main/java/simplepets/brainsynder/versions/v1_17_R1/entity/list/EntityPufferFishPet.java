package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.PufferState;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityFishPet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPufferFish}
 */
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet {
    private static final DataWatcherObject<Integer> PUFF_STATE;

    public EntityPufferFishPet(PetType type, PetUser user) {
        super(EntityTypes.PUFFERFISH, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("size", getPuffState());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("size")) setPuffState(object.getEnum("size", PufferState.class, PufferState.SMALL));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(PUFF_STATE, 0);
    }

    @Override
    public PufferState getPuffState() {
        return PufferState.getByID(datawatcher.get(PUFF_STATE));
    }

    @Override
    public void setPuffState(PufferState state) {
        datawatcher.set(PUFF_STATE, state.ordinal());
    }

    static {
        PUFF_STATE = DataWatcher.a(EntityPufferFishPet.class, DataWatcherWrapper.INT);
    }
}
