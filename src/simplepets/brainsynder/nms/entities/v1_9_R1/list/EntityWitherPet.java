package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityWitherPet;
import simplepets.brainsynder.nms.entities.v1_9_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityWitherPet extends EntityPet implements IEntityWitherPet {
    private static final DataWatcherObject<Integer> a;
    private static final DataWatcherObject<Integer> b;
    private static final DataWatcherObject<Integer> c;
    private static final DataWatcherObject<Integer> SHIELDED;

    static {
        a = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
        b = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
        c = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
        SHIELDED = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
    }

    public EntityWitherPet(World world, IPet pet) {
        super(world, pet);
    }


    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("shielded", isShielded());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("shielded")) {
            setShielded(object.getBoolean("shielded"));
        }
        super.applyCompound(object);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(a, Integer.valueOf(0));
        this.datawatcher.register(b, Integer.valueOf(0));
        this.datawatcher.register(c, Integer.valueOf(0));
        this.datawatcher.register(SHIELDED, Integer.valueOf(0));
    }

    @Override
    public boolean isShielded() {
        return this.datawatcher.get(SHIELDED) == 1;
    }

    @Override
    public void setShielded(boolean flag) {
        this.datawatcher.set(SHIELDED, flag ? 1 : 0);
        this.setHealth((float) (flag ? 150 : 300));
    }

}
