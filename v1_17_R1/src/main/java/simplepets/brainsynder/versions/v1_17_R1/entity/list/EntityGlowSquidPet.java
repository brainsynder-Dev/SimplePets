package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityGlowSquidPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGlowSquid}
 */
public class EntityGlowSquidPet extends EntitySquidPet implements IEntityGlowSquidPet {
    private static final DataWatcherObject<Integer> DATA_DARK_TICKS_REMAINING;

    public EntityGlowSquidPet(PetType type, PetUser user) {
        super(EntityTypes.SQUID, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setBoolean("glowing", isSquidGlowing());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("glowing")) setSquidGlowing(object.getBoolean("glowing"));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(DATA_DARK_TICKS_REMAINING, 0);
    }

    @Override
    public boolean isSquidGlowing() {
        return datawatcher.get(DATA_DARK_TICKS_REMAINING) == 0;
    }

    @Override
    public void setSquidGlowing(boolean glowing) {
        datawatcher.set(DATA_DARK_TICKS_REMAINING, glowing ? 0 : 100);
    }

    static {
        DATA_DARK_TICKS_REMAINING = DataWatcher.a(EntityGlowSquidPet.class, DataWatcherWrapper.INT);
    }
}
