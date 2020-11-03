package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityHoglinPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityHoglin}
 */
@Size(width = 1.3964844F, length = 1.4F)
public class EntityHoglinPet extends AgeableEntityPet implements IEntityHoglinPet {
    private static DataWatcherObject<Boolean> IMMUNE_TO_ZOMBIFICATION;
    private static boolean registered = false;

    static {
        IMMUNE_TO_ZOMBIFICATION = DataWatcher.a(EntityHoglinPet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityHoglinPet(World world, IPet pet) {
        super(EntityTypes.HOGLIN, world, pet);
    }
    public EntityHoglinPet(World world) {
        super(EntityTypes.HOGLIN, world);
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
