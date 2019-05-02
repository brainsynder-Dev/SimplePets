package simplepets.brainsynder.nms.v1_14_R1.entities.branch;

import net.minecraft.server.v1_14_R1.*;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.EntityPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityRaider}
 */
public abstract class EntityRaiderPet extends EntityPet {
    private static final DataWatcherObject<Boolean> CELEBRATING;

    static {
        CELEBRATING = DataWatcher.a(EntityRaiderPet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityRaiderPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityRaiderPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CELEBRATING, false);
    }
}
