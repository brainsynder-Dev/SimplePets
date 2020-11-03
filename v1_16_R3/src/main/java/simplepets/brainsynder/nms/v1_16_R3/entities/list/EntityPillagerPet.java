package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.branch.EntityIllagerAbstractPet;
import simplepets.brainsynder.nms.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityPillager}
 */
@Size(width = 0.6F, length = 1.95F)
public class EntityPillagerPet extends EntityIllagerAbstractPet implements IEntityPillagerPet {
    private static final DataWatcherObject<Boolean> CHARGING;

    static {
        CHARGING = DataWatcher.a(EntityPillagerPet.class, DataWatcherWrapper.BOOLEAN);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(CHARGING, false);
    }

    public EntityPillagerPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityPillagerPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
