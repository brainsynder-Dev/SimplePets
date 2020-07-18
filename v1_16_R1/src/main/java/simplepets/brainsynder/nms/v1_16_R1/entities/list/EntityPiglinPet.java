package simplepets.brainsynder.nms.v1_16_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R1.*;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_16_R1.utils.DataWatcherWrapper;


/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntityPiglin}
 */
public class EntityPiglinPet extends AgeableEntityPet implements IEntityPiglinPet {
    private static final DataWatcherObject<Boolean> CHARGING;
    private static final DataWatcherObject<Boolean> DANCING;
    private static final DataWatcherObject<Boolean> IMMUNE_TO_ZOMBIFICATION;

    static {
        CHARGING = DataWatcher.a(EntityPiglinPet.class, DataWatcherWrapper.BOOLEAN);
        DANCING = DataWatcher.a(EntityPiglinPet.class, DataWatcherWrapper.BOOLEAN);
        IMMUNE_TO_ZOMBIFICATION = DataWatcher.a(EntityPiglinPet.class, DataWatcherWrapper.BOOLEAN);
    }
    public EntityPiglinPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityPiglinPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CHARGING, false);
        this.datawatcher.register(DANCING, false);
        this.datawatcher.register(IMMUNE_TO_ZOMBIFICATION, true); // Makes them not shake by default
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("charging", isCharging());
        object.setBoolean("dancing", isDancing());
        object.setBoolean("shaking", isShaking());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("charging")) setCharging(object.getBoolean("charging"));
        if (object.hasKey("dancing")) setDancing(object.getBoolean("dancing"));
        if (object.hasKey("shaking")) setShaking(object.getBoolean("shaking"));
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
    public boolean isShaking() {
        return !datawatcher.get(IMMUNE_TO_ZOMBIFICATION);
    }

    @Override
    public void setShaking(boolean value) {
        datawatcher.set(IMMUNE_TO_ZOMBIFICATION, !value);
    }
}
