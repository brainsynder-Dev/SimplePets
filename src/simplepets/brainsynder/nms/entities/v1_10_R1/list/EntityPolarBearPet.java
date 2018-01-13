package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.AgeableEntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityPolarBearPet extends AgeableEntityPet implements IEntityPolarBearPet {
    private static final DataWatcherObject<Boolean> STANDING_UP;

    static {
        STANDING_UP = DataWatcher.a(EntityPolarBearPet.class, DataWatcherRegistry.h);
    }

    public EntityPolarBearPet(World world, IPet pet) {
        super(world, pet);
    }

    public void setStandingUp(boolean flag) {
        this.datawatcher.set(STANDING_UP, flag);
    }

    @Override
    public boolean isStanding() {
        return datawatcher.get(STANDING_UP);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(STANDING_UP, false);
    }
}
