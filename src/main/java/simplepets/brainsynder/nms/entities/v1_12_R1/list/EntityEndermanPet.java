package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import com.google.common.base.Optional;
import net.minecraft.server.v1_12_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;

@Size(width = 0.6F, length = 2.9F)
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet {
    private static final DataWatcherObject<Optional<IBlockData>> CARRIED_BLOCK;
    private static final DataWatcherObject<Boolean> SCREAMING;

    static {
        CARRIED_BLOCK = DataWatcher.a(EntityEndermanPet.class, DataWatcherRegistry.g);
        SCREAMING = DataWatcher.a(EntityEndermanPet.class, DataWatcherRegistry.h);
    }

    public EntityEndermanPet(World world) {
        super(world);
    }

    public EntityEndermanPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CARRIED_BLOCK, Optional.absent());
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
