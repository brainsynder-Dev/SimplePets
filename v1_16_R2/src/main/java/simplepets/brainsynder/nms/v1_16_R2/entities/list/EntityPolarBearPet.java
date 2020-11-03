package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityPolarBear}
 */
@Size(width = 1.3F, length = 1.4F)
public class EntityPolarBearPet extends AgeableEntityPet implements IEntityPolarBearPet {
    private static final DataWatcherObject<Boolean> IS_STANDING;

    static {
        IS_STANDING = DataWatcher.a(EntityPolarBearPet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityPolarBearPet(World world) {
        super(EntityTypes.POLAR_BEAR, world);
    }

    public EntityPolarBearPet(World world, IPet pet) {
        super(EntityTypes.POLAR_BEAR, world, pet);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(IS_STANDING, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("standing", isStanding());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("standing"))
            setStandingUp(object.getBoolean("standing"));
        super.applyCompound(object);
    }

    public void setStandingUp(boolean flag) {
        this.datawatcher.set(IS_STANDING, flag);
    }

    @Override
    public boolean isStanding() {
        return this.datawatcher.get(IS_STANDING);
    }
}
