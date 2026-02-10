package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityDolphinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Dolphin}
 */
public class EntityDolphinPet extends EntityPetOverride implements IEntityDolphinPet {
    private static final EntityDataAccessor<BlockPos> TREASURE = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> HAS_FISH = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MOIST = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.INT);

    public EntityDolphinPet(PetType type, PetUser user) {
        super(EntityType.DOLPHIN, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(TREASURE, BlockPos.ZERO);
        dataAccess.define(HAS_FISH, false);
        dataAccess.define(MOIST, 2400);
    }
}
