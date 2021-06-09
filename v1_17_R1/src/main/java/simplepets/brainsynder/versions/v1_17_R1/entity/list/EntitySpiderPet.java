package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntitySpiderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySpider}
 */
public class EntitySpiderPet extends EntityPet implements IEntitySpiderPet {
    private static final DataWatcherObject<Byte> WALL_CLIMB_FLAG;

    public EntitySpiderPet(PetType type, PetUser user) {
        super(EntityTypes.SPIDER, type, user);
    }

    @Override
    public boolean isClimbing() {
        return canWallClimb();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClientSide) this.setWallClimb(this.positionChanged);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(WALL_CLIMB_FLAG, (byte)0);
    }

    static {
        WALL_CLIMB_FLAG = DataWatcher.a(EntitySpiderPet.class, DataWatcherWrapper.BYTE);
    }

    public boolean canWallClimb() {
        return (this.datawatcher.get(WALL_CLIMB_FLAG) & 1) != 0;
    }

    public void setWallClimb(boolean wallClimb) {
        byte value = this.datawatcher.get(WALL_CLIMB_FLAG);
        if (wallClimb) {
            value = (byte)(value | 1);
        } else {
            value &= -2;
        }

        this.datawatcher.set(WALL_CLIMB_FLAG, value);

    }
}
