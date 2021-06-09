package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.entity.hostile.IEntityDolphinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityDolphin}
 */
public class EntityDolphinPet extends EntityPet implements IEntityDolphinPet {
    private static final DataWatcherObject<BlockPosition> TREASURE;
    private static final DataWatcherObject<Boolean> HAS_FISH;
    private static final DataWatcherObject<Integer> MOIST;

    public EntityDolphinPet(PetType type, PetUser user) {
        super(EntityTypes.DOLPHIN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TREASURE, BlockPosition.ZERO);
        this.datawatcher.register(HAS_FISH, false);
        this.datawatcher.register(MOIST, 2400);
    }

    static {
        TREASURE = DataWatcher.a(EntityDolphin.class, DataWatcherWrapper.BLOCK_POS);
        HAS_FISH = DataWatcher.a(EntityDolphin.class, DataWatcherWrapper.BOOLEAN);
        MOIST = DataWatcher.a(EntityDolphin.class, DataWatcherWrapper.INT);
    }
}
