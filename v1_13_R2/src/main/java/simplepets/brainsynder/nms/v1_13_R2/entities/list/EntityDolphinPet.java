package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.*;
import simplepets.brainsynder.api.entity.hostile.IEntityDolphinPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_13_R2.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityDolphin}
 */
public class EntityDolphinPet extends EntityPet implements IEntityDolphinPet {
    private static final DataWatcherObject<BlockPosition> TREASURE;
    private static final DataWatcherObject<Boolean> HAS_FISH;
    private static final DataWatcherObject<Integer> MOIST;

    public EntityDolphinPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
        this.lookController = new ControllerLookDolphin(this, 10);
    }

    public EntityDolphinPet(EntityTypes<?> type, World world) {
        super(type,world);
        this.lookController = new ControllerLookDolphin(this, 10);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TREASURE, BlockPosition.ZERO);
        this.datawatcher.register(HAS_FISH, false);
        this.datawatcher.register(MOIST, 2400);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packetPlayOutRelEntityMoveLook = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook();

    }

    static {
        TREASURE = DataWatcher.a(EntityDolphin.class, DataWatcherWrapper.BLOCK_POS);
        HAS_FISH = DataWatcher.a(EntityDolphin.class, DataWatcherWrapper.BOOLEAN);
        MOIST = DataWatcher.a(EntityDolphin.class, DataWatcherWrapper.INT);
    }
}
