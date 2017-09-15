package simplepets.brainsynder.nms.entities.v1_10_R1.list;


import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntitySlimePet;
import simplepets.brainsynder.nms.entities.v1_10_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntitySlimePet extends EntityPet implements IEntitySlimePet {
    private static final DataWatcherObject<Integer> SIZE;

    static {
        SIZE = DataWatcher.a(EntitySlimePet.class, DataWatcherRegistry.b);
    }

    public EntitySlimePet(World world, IPet pet) {
        super(world, pet);
        setSize(2);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("Size", getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Size")) {
            setSize(object.getInteger("Size"));
        }
        super.applyCompound(object);
    }


    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(SIZE, 1);
    }

    public int getSize() {
        return this.datawatcher.get(SIZE);
    }

    public void setSize(int i) {
        this.datawatcher.set(SIZE, i);
    }

    public boolean isSmall() {
        return this.getSize() <= 1;
    }
}
