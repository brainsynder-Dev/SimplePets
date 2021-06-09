package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityStriderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityStrider}
 */
public class EntityStriderPet extends EntityAgeablePet implements IEntityStriderPet {
    private static DataWatcherObject<Integer> BOOST_TIME;
    private static DataWatcherObject<Boolean> COLD;
    private static DataWatcherObject<Boolean> SADDLED;
    private static boolean registered = false;

    public EntityStriderPet(PetType type, PetUser user) {
        super(EntityTypes.STRIDER, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("cold", isCold());
        object.setBoolean("saddled", isSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("cold")) setCold(object.getBoolean("cold"));
        if (object.hasKey("saddled")) setSaddled(object.getBoolean("saddled"));
        super.applyCompound(object);
    }

    @Override
    public boolean isSaddled() {
        return datawatcher.get(SADDLED);
    }

    @Override
    public void setSaddled(boolean saddled) {
        datawatcher.set(SADDLED, saddled);
    }

    @Override
    public boolean isCold() {
        return datawatcher.get(COLD);
    }

    @Override
    public void setCold(boolean cold) {
        datawatcher.set(COLD, cold);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        if (!registered) {
            BOOST_TIME = DataWatcher.a(EntityStriderPet.class, DataWatcherWrapper.INT);
            COLD = DataWatcher.a(EntityStriderPet.class, DataWatcherWrapper.BOOLEAN);
            SADDLED = DataWatcher.a(EntityStriderPet.class, DataWatcherWrapper.BOOLEAN);
            registered = true;
        }
        this.datawatcher.register(BOOST_TIME, 0);
        this.datawatcher.register(COLD, false);
        this.datawatcher.register(SADDLED, false);
    }
}
