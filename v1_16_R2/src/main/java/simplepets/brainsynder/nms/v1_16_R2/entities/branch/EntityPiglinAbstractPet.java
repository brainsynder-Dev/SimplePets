package simplepets.brainsynder.nms.v1_16_R2.entities.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R2.*;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;


/**
 * NMS: {@link EntityPiglin}
 */
public class EntityPiglinAbstractPet extends EntityPet implements IShaking {
    private static final DataWatcherObject<Boolean> IMMUNE_TO_ZOMBIFICATION;

    static {
        IMMUNE_TO_ZOMBIFICATION = DataWatcher.a(EntityPiglinAbstractPet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityPiglinAbstractPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityPiglinAbstractPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(IMMUNE_TO_ZOMBIFICATION, true); // Makes them not shake by default
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
