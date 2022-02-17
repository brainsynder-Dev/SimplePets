package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntitySpiderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySpider}
 */
public class EntitySpiderPet extends EntityPet implements IEntitySpiderPet {
    private static final EntityDataAccessor<Byte> WALL_CLIMB_FLAG;
    private final boolean wallClimbing;

    public EntitySpiderPet(PetType type, PetUser user) {
        super(EntityType.SPIDER, type, user);
        wallClimbing = ConfigOption.INSTANCE.PET_TOGGLES_SPIDER_CLIMB.getValue();
    }

    @Override
    public boolean onClimbable() {
        return canWallClimb();
    }

    @Override
    public void tick() {
        super.tick();
        if ((!this.level.isClientSide) && wallClimbing) this.setWallClimb(this.horizontalCollision);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(WALL_CLIMB_FLAG, (byte)0);
    }

    static {
        WALL_CLIMB_FLAG = SynchedEntityData.defineId(EntitySpiderPet.class, EntityDataSerializers.BYTE);
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
