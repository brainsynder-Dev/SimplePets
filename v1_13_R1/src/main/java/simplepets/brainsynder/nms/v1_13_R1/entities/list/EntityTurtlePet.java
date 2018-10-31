package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityTurtlePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_13_R1.registry.Types;

@Size(width = 0.9F, length = 1.3F)
public class EntityTurtlePet extends AgeableEntityPet implements IEntityTurtlePet {
    private static final DataWatcherObject<BlockPosition> HOME;
    private static final DataWatcherObject<Boolean> HAS_EGG;
    private static final DataWatcherObject<Boolean> bG;
    private static final DataWatcherObject<BlockPosition> TRAVEL;
    private static final DataWatcherObject<Boolean> bI;
    private static final DataWatcherObject<Boolean> bJ;

    public EntityTurtlePet(World world, IPet pet) {
        super(Types.TURTLE, world, pet);
    }
    public EntityTurtlePet(World world) {
        super(Types.TURTLE, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(HOME, BlockPosition.ZERO);
        this.datawatcher.register(HAS_EGG, false);
        this.datawatcher.register(TRAVEL, BlockPosition.ZERO);
        this.datawatcher.register(bI, false);
        this.datawatcher.register(bJ, false);
        this.datawatcher.register(bG, false);
    }

    static {
        HOME = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.l);
        HAS_EGG = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
        bG = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
        TRAVEL = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.l);
        bI = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
        bJ = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
    }
}
