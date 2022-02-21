package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityDolphinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityDolphin}
 */
public class EntityDolphinPet extends EntityPet implements IEntityDolphinPet {
    private static final EntityDataAccessor<BlockPos> TREASURE;
    private static final EntityDataAccessor<Boolean> HAS_FISH;
    private static final EntityDataAccessor<Integer> MOIST;

    public EntityDolphinPet(PetType type, PetUser user) {
        super(EntityType.DOLPHIN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(TREASURE, BlockPos.ZERO);
        this.entityData.define(HAS_FISH, false);
        this.entityData.define(MOIST, 2400);
    }

    static {
        TREASURE = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.BLOCK_POS);
        HAS_FISH = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.BOOLEAN);
        MOIST = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.INT);
    }
}
