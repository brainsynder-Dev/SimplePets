package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityFishPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;
import simplepets.brainsynder.wrapper.PufferState;

@Size(length = 0.7F, width = 0.7F)
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet {
    private static final DataWatcherObject<Integer> PUFF_STATE;

    public EntityPufferFishPet(World world, IPet pet) {
        super(Types.PUFFER_FISH, world, pet);
    }
    public EntityPufferFishPet(World world) {
        super(Types.PUFFER_FISH, world);
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
        PUFF_STATE = DataWatcher.a(EntityPufferFishPet.class, DataWatcherRegistry.b);
    }
}
