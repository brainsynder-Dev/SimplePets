package simplepets.brainsynder.nms.entity.list;

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
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIGGING_SAND = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);

    public EntityTurtlePet(PetType type, PetUser user) {
        super(EntityType.TURTLE, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(HAS_EGG, false);
        dataAccess.define(DIGGING_SAND, false);
    }
}
