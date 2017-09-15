package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityVexPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityNoClipPet;
import simplepets.brainsynder.pet.IPet;

public class EntityVexPet extends EntityNoClipPet implements IEntityVexPet {
    protected static final DataWatcherObject<Byte> DATA;

    static {
        DATA = DataWatcher.a(EntityVexPet.class, DataWatcherRegistry.a);
    }


    public EntityVexPet(World world) {
        super(world);
    }

    public EntityVexPet(World world, IPet pet) {
        super(world, pet);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.getDataWatcher().register(DATA, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Powered", isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Powered")) {
            setPowered(object.getBoolean("Powered"));
        }
        super.applyCompound(object);
    }

    private void setData(int i, boolean flag) {
        byte b0 = this.datawatcher.get(DATA);
        int j;
        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.datawatcher.set(DATA, (byte) (j & 255));
    }

    public boolean isPowered() {
        return this.getData(1);
    }

    public void setPowered(boolean flag) {
        this.setData(1, flag);
    }

    private boolean getData(int i) {
        byte b0 = this.datawatcher.get(DATA);
        return (b0 & i) != 0;
    }
}