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

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityTurtle}
 */
public class EntityTurtlePet extends EntityAgeablePet implements IEntityTurtlePet {
    private static final EntityDataAccessor<BlockPos> HOME_POS;
    private static final EntityDataAccessor<Boolean> HAS_EGG;
    private static final EntityDataAccessor<Boolean> DIGGING_SAND;
    private static final EntityDataAccessor<BlockPos> TRAVEL_POS;
    private static final EntityDataAccessor<Boolean> LAND_BOUND;
    private static final EntityDataAccessor<Boolean> ACTIVELY_TRAVELLING;

    public EntityTurtlePet(PetType type, PetUser user) {
        super(EntityType.TURTLE, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(HOME_POS, BlockPos.ZERO);
        this.entityData.define(HAS_EGG, false);
        this.entityData.define(TRAVEL_POS, BlockPos.ZERO);
        this.entityData.define(LAND_BOUND, false);
        this.entityData.define(ACTIVELY_TRAVELLING, false);
        this.entityData.define(DIGGING_SAND, false);
    }

    static {
        HOME_POS = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);
        HAS_EGG = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
        DIGGING_SAND = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
        TRAVEL_POS = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);
        LAND_BOUND = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
        ACTIVELY_TRAVELLING = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
    }
}
