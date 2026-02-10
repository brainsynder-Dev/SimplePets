package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntitySpiderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.Spider}
 */
public class EntitySpiderPet extends EntityPetOverride implements IEntitySpiderPet {
    private static final EntityDataAccessor<Byte> WALL_CLIMB_FLAG = SynchedEntityData.defineId(EntitySpiderPet.class, EntityDataSerializers.BYTE);
    private final boolean wallClimbing;

    public EntitySpiderPet(PetType type, PetUser user) {
        super(EntityType.SPIDER, type, user);
        wallClimbing = ConfigOption.INSTANCE.PET_TOGGLES_SPIDER_CLIMB.getValue();
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(WALL_CLIMB_FLAG, (byte)0);
    }

    @Override
    public boolean onClimbable() {
        return canWallClimb();
    }

    @Override
    public void tick() {
        super.tick();
        if ((!VersionTranslator.getEntityLevel(this).isClientSide()) && wallClimbing) this.setWallClimb(this.horizontalCollision);
    }

    public boolean canWallClimb() {
        if (!wallClimbing) return false;
        return (this.entityData.get(WALL_CLIMB_FLAG) & 1) != 0;
    }

    public void setWallClimb(boolean wallClimb) {
        byte value = this.entityData.get(WALL_CLIMB_FLAG);
        if (wallClimb) {
            value = (byte)(value | 1);
        } else {
            value &= -2;
        }

        this.entityData.set(WALL_CLIMB_FLAG, value);

    }
}
