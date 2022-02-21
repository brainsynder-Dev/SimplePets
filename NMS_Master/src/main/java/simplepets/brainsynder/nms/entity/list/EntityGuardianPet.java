package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.hostile.IEntityGuardianPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGuardian}
 */
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet {
    private static final EntityDataAccessor<Boolean> MOVING;
    private static final EntityDataAccessor<Integer> TARGET_ENTITY;

    public EntityGuardianPet(PetType type, PetUser user) {
        super(EntityType.GUARDIAN, type, user);
    }
    public EntityGuardianPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(MOVING, Boolean.FALSE);
        this.entityData.define(TARGET_ENTITY, 0);
    }

    static {
        MOVING = SynchedEntityData.defineId(EntityGuardianPet.class, EntityDataSerializers.BOOLEAN);
        TARGET_ENTITY = SynchedEntityData.defineId(EntityGuardianPet.class, EntityDataSerializers.INT);
    }
}
