package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityGhastPet;
import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntityGhastPet extends EntityPet implements IEntityGhastPet,
        IFlyablePet {
    private static final DataWatcherObject<Boolean> a;

    public EntityGhastPet(World world) {
        super(world);
    }
    public EntityGhastPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(a, Boolean.FALSE);
    }

    static {
        a = DataWatcher.a(EntityGhastPet.class, DataWatcherRegistry.h);
    }
}
