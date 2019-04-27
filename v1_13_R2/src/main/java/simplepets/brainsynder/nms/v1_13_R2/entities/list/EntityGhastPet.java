package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.entity.misc.IFlyablePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_13_R2.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityGhast}
 */
@Size(width = 4.0F, length = 4.0F)
public class EntityGhastPet extends EntityPet implements IEntityGhastPet,
        IFlyablePet {
    private static final DataWatcherObject<Boolean> ATTACKING;

    static {
        ATTACKING = DataWatcher.a(EntityGhastPet.class, DataWatcherWrapper.BOOLEAN);
    }

    public EntityGhastPet(EntityTypes<?> type, World world) {
        super(type, world);
    }

    public EntityGhastPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    public boolean isScreaming() {
        return datawatcher.get(ATTACKING);
    }

    @Override
    public void setScreaming(boolean var) {
        datawatcher.set(ATTACKING, var);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(ATTACKING, Boolean.FALSE);
    }
}
