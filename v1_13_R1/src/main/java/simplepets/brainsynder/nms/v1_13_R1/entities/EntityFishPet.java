package simplepets.brainsynder.nms.v1_13_R1.entities;

import net.minecraft.server.v1_13_R1.*;
import simplepets.brainsynder.api.entity.IEntityFishPet;
import simplepets.brainsynder.api.pet.IPet;

public abstract class EntityFishPet extends EntityPet implements IEntityFishPet {
    private static final DataWatcherObject<Boolean> FROM_BUCKET;

    public EntityFishPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    public EntityFishPet(EntityTypes<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(FROM_BUCKET, false);
    }

    static {
        FROM_BUCKET = DataWatcher.a(EntityFishPet.class, DataWatcherRegistry.i);
    }
}
