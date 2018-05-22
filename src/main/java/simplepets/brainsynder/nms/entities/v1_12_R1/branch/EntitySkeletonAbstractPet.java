package simplepets.brainsynder.nms.entities.v1_12_R1.branch;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.entity.ISkeletonAbstract;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;

public abstract class EntitySkeletonAbstractPet extends EntityPet implements ISkeletonAbstract {
    private static final DataWatcherObject<Boolean> SWINGING_ARMS;

    static {
        SWINGING_ARMS = DataWatcher.a(EntitySkeletonAbstractPet.class, DataWatcherRegistry.h);
    }


    public EntitySkeletonAbstractPet(World world) {
        super(world);
    }

    public EntitySkeletonAbstractPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SWINGING_ARMS, true);
    }

    @Override
    public boolean isArmsSwinging() {
        return datawatcher.get(SWINGING_ARMS);
    }

    @Override
    public void setArmsSwinging(boolean flag) {
        datawatcher.set(SWINGING_ARMS, flag);
    }
}
