package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.EntityPet;
import simplepets.brainsynder.nms.v1_16_R3.utils.DataWatcherWrapper;

import java.util.Optional;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityEnderman}
 */
@Size(width = 0.6F, length = 2.9F)
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet {
    private static final DataWatcherObject<Optional<IBlockData>> CARRIED_BLOCK;
    private static final DataWatcherObject<Boolean> SCREAMING;

    static {
        CARRIED_BLOCK = DataWatcher.a(EntityEndermanPet.class, DataWatcherWrapper.BLOCK);
        SCREAMING = DataWatcher.a(EntityEndermanPet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityEndermanPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityEndermanPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CARRIED_BLOCK, Optional.empty());
        this.datawatcher.register(SCREAMING, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("screaming", isScreaming());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
        super.applyCompound(object);
    }

    public boolean isScreaming() {
        return this.datawatcher.get(SCREAMING);
    }

    public void setScreaming(boolean flag) {
        this.datawatcher.set(SCREAMING, flag);
    }
}
