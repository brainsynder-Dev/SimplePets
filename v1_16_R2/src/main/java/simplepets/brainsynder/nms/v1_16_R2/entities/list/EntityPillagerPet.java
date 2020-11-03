package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.branch.EntityIllagerAbstractPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;

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

    public EntityPillagerPet(World world) {
        super(EntityTypes.PILLAGER, world);
    }
    public EntityPillagerPet(World world, IPet pet) {
        super(EntityTypes.PILLAGER, world, pet);
    }
}
