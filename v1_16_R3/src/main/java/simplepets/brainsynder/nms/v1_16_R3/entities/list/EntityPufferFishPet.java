package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.EntityFishPet;
import simplepets.brainsynder.nms.v1_16_R3.utils.DataWatcherWrapper;
import simplepets.brainsynder.wrapper.PufferState;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPufferFish}
 */
@Size(length = 0.7F, width = 0.7F)
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet {
    private static final DataWatcherObject<Integer> PUFF_STATE;

    public EntityPufferFishPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityPufferFishPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(PUFF_STATE, 0);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        this.setAirTicks(300);
    }

    @Override
    public PufferState getPuffState() {
        return PufferState.getByID(datawatcher.get(PUFF_STATE));
    }

    @Override
    public void setPuffState(PufferState state) {
        datawatcher.set(PUFF_STATE, state.ordinal());
    }

    static {
        PUFF_STATE = DataWatcher.a(EntityPufferFishPet.class, DataWatcherWrapper.INT);
    }
}
