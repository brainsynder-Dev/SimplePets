package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.entity.misc.IFlyablePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.EntityPet;

public class EntityGhastPet extends EntityPet implements IEntityGhastPet,
        IFlyablePet {
    private static final DataWatcherObject<Boolean> a;

    static {
        a = DataWatcher.a(EntityGhastPet.class, DataWatcherRegistry.h);
    }


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
}
