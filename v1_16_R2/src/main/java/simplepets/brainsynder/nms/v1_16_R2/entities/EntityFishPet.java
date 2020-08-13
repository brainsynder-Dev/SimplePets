package simplepets.brainsynder.nms.v1_16_R2.entities;

import net.minecraft.server.v1_16_R2.*;
import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;

public abstract class EntityFishPet extends EntityPet implements IEntityFishPet {
    private static final DataWatcherObject<Boolean> FROM_BUCKET;

    public EntityFishPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    public EntityFishPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(FROM_BUCKET, false);
    }

    static {
        FROM_BUCKET = DataWatcher.a(EntityFishPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
