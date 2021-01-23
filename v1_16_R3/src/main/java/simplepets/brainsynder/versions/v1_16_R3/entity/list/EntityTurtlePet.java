package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityTurtlePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityTurtlePet extends EntityAgeablePet implements IEntityTurtlePet {
    private static final DataWatcherObject<BlockPosition> HOME_POS;
    private static final DataWatcherObject<Boolean> HAS_EGG;
    private static final DataWatcherObject<Boolean> DIGGING_SAND;
    private static final DataWatcherObject<BlockPosition> TRAVEL_POS;
    private static final DataWatcherObject<Boolean> LAND_BOUND;
    private static final DataWatcherObject<Boolean> ACTIVELY_TRAVELLING;

    public EntityTurtlePet(PetType type, PetUser user) {
        super(EntityTypes.TURTLE, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(HOME_POS, BlockPosition.ZERO);
        this.datawatcher.register(HAS_EGG, false);
        this.datawatcher.register(TRAVEL_POS, BlockPosition.ZERO);
        this.datawatcher.register(LAND_BOUND, false);
        this.datawatcher.register(ACTIVELY_TRAVELLING, false);
        this.datawatcher.register(DIGGING_SAND, false);
    }

    static {
        HOME_POS = DataWatcher.a(EntityTurtlePet.class, DataWatcherWrapper.BLOCK_POS);
        HAS_EGG = DataWatcher.a(EntityTurtlePet.class, DataWatcherWrapper.BOOLEAN);
        DIGGING_SAND = DataWatcher.a(EntityTurtlePet.class, DataWatcherWrapper.BOOLEAN);
        TRAVEL_POS = DataWatcher.a(EntityTurtlePet.class, DataWatcherWrapper.BLOCK_POS);
        LAND_BOUND = DataWatcher.a(EntityTurtlePet.class, DataWatcherWrapper.BOOLEAN);
        ACTIVELY_TRAVELLING = DataWatcher.a(EntityTurtlePet.class, DataWatcherWrapper.BOOLEAN);
    }
}
