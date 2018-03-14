package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityCreeperPet;
import simplepets.brainsynder.nms.entities.v1_9_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet {
    private static final DataWatcherObject<Integer> a;
    private static final DataWatcherObject<Boolean> POWERED;
    private static final DataWatcherObject<Boolean> IGNITED;

    static {
        a = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.b);
        POWERED = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.h);
        IGNITED = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.h);
    }

    public EntityCreeperPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("powered", isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("powered")) {
            setPowered(object.getBoolean("powered"));
        }
        super.applyCompound(object);
    }

    public void setIgnited(boolean flag) {
        this.datawatcher.set(IGNITED, flag);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(a, -1);
        this.datawatcher.register(POWERED, false);
        this.datawatcher.register(IGNITED, false);
    }

    @Override
    public boolean isPowered() {
        return datawatcher.get(POWERED);
    }

    @Override
    public void setPowered(boolean flag) {
        this.datawatcher.set(POWERED, flag);

    }
}
