package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityBlazePet extends EntityPet implements IEntityBlazePet {
    private static final DataWatcherObject<Byte> c;

    static {
        c = DataWatcher.a(EntityBlazePet.class, DataWatcherRegistry.a);
    }

    public EntityBlazePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("burning", isBurning());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("burning")) {
            setBurning(object.getBoolean("burning"));
        }
        super.applyCompound(object);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(c, (byte) 0);
    }

    @Override
    public boolean isBurning() {
        return (this.datawatcher.get(c) & 1) != 0;
    }

    @Override
    public void setBurning(boolean var1) {
        byte var2 = this.datawatcher.get(c);
        if (var1) {
            var2 = (byte) (var2 | 1);
        } else {
            var2 &= -2;
        }

        this.datawatcher.set(c, var2);
    }
}
