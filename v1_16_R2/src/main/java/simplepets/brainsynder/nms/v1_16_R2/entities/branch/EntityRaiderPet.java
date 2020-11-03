package simplepets.brainsynder.nms.v1_16_R2.entities.branch;

import net.minecraft.server.v1_16_R2.*;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityRaider}
 */
public abstract class EntityRaiderPet extends EntityPet {
    private static final DataWatcherObject<Boolean> CELEBRATING;

    static {
        CELEBRATING = DataWatcher.a(EntityRaiderPet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityRaiderPet(EntityTypes<? extends EntityInsentient> type, World world) {
        super(type, world);
    }

    public EntityRaiderPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CELEBRATING, false);
    }
}
