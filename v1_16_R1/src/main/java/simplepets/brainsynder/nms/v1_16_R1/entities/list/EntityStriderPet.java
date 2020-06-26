package simplepets.brainsynder.nms.v1_16_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R1.*;
import simplepets.brainsynder.api.entity.passive.IEntityStriderPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_16_R1.utils.DataWatcherWrapper;


/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntityStrider}
 */
public class EntityStriderPet extends AgeableEntityPet implements IEntityStriderPet {
    private static final DataWatcherObject<Integer> BOOST_TIME;
    private static final DataWatcherObject<Boolean> COLD;
    private static final DataWatcherObject<Boolean> SADDLED;

    static {
        BOOST_TIME = DataWatcher.a(EntityStriderPet.class, DataWatcherWrapper.INT);
        COLD = DataWatcher.a(EntityStriderPet.class, DataWatcherWrapper.BOOLEAN);
        SADDLED = DataWatcher.a(EntityStriderPet.class, DataWatcherWrapper.BOOLEAN);
    }
    public EntityStriderPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityStriderPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(BOOST_TIME, 0);
        this.datawatcher.register(COLD, true);
        this.datawatcher.register(SADDLED, false);
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
}
