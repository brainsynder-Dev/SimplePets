package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.DataWatcher;
import net.minecraft.server.v1_9_R2.DataWatcherObject;
import net.minecraft.server.v1_9_R2.DataWatcherRegistry;
import net.minecraft.server.v1_9_R2.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityBlazePet;
import simplepets.brainsynder.nms.entities.v1_9_R2.EntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntityBlazePet extends EntityPet implements IEntityBlazePet {
    private static final DataWatcherObject<Byte> ANGERED;

    static {
        ANGERED = DataWatcher.a(EntityBlazePet.class, DataWatcherRegistry.a);
    }

    public EntityBlazePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Burning", isBurning());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Burning")) {
            setBurning(object.getBoolean("Burning"));
        }
        super.applyCompound(object);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(ANGERED, (byte) 0);
    }

    @Override
    public boolean isBurning() {
        return (this.datawatcher.get(ANGERED) & 1) != 0;
    }

    @Override
    public void setBurning(boolean var1) {
        byte b1 = this.datawatcher.get(ANGERED).byteValue();
        if (var1) {
            b1 = (byte) (b1 | 1);
        } else {
            b1 &= -2;
        }

        this.datawatcher.set(ANGERED, Byte.valueOf(b1));
    }
}
