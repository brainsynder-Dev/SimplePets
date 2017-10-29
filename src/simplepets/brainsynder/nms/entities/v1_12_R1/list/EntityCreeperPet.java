package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityCreeperPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet {
    protected static final DataWatcherObject<Integer> a;
    protected static final DataWatcherObject<Boolean> POWERED;
    protected static final DataWatcherObject<Boolean> IGNITED;

    public EntityCreeperPet(World world) {
        super(world);
    }
    public EntityCreeperPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(a, -1);
        this.datawatcher.register(POWERED, false);
        this.datawatcher.register(IGNITED, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("powered", isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("powered")) setPowered(object.getBoolean("powered"));
        super.applyCompound(object);
    }

    @Override
    public boolean isIgnited() {
        return datawatcher.get(IGNITED);
    }

    @Override
    public void setIgnited(boolean flag) {
        this.datawatcher.set(IGNITED, flag);
    }

    @Override
    public boolean isPowered() {
        return datawatcher.get(POWERED);
    }

    @Override
    public void setPowered(boolean flag) {
        this.datawatcher.set(POWERED, flag);
    }

    static {
        a = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.b);
        POWERED = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.h);
        IGNITED = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.h);
    }
}
