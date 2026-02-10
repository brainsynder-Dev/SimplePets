package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityTurtlePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Turtle}
 */
public class EntityTurtlePet extends EntityAgeablePet implements IEntityTurtlePet {
    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIGGING_SAND = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> TRAVEL_POS = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> LAND_BOUND = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ACTIVELY_TRAVELLING = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);

    public EntityTurtlePet(PetType type, PetUser user) {
        super(EntityType.TURTLE, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(HOME_POS, BlockPos.ZERO);
        dataAccess.define(HAS_EGG, false);
        dataAccess.define(TRAVEL_POS, BlockPos.ZERO);
        dataAccess.define(LAND_BOUND, false);
        dataAccess.define(ACTIVELY_TRAVELLING, false);
        dataAccess.define(DIGGING_SAND, false);
    }
}
