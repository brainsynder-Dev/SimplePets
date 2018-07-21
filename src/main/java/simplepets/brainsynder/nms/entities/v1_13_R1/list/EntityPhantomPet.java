package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.DataWatcher;
import net.minecraft.server.v1_13_R1.DataWatcherObject;
import net.minecraft.server.v1_13_R1.DataWatcherRegistry;
import net.minecraft.server.v1_13_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPhantomPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.EntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;


@Size(width = 0.6F, length = 0.6F)
public class EntityPhantomPet extends EntityPet implements IEntityPhantomPet {
    private static final DataWatcherObject<Integer> SIZE;

    public EntityPhantomPet(World world, IPet pet) {
        super(Types.PHANTOM, world, pet);
    }
    public EntityPhantomPet(World world) {
        super(Types.PHANTOM, world);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SIZE, 1);
    }

    @Override
    public int getSize() {
        return datawatcher.get(SIZE);
    }

    @Override
    public void setSize(int i) {
        datawatcher.set(SIZE, i);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("size", getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("size")) setSize(object.getInteger("size"));
        super.applyCompound(object);
    }

    static {
        SIZE = DataWatcher.a(EntitySlimePet.class, DataWatcherRegistry.b);
    }
}
