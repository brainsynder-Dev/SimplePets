package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.EntityPet;
import simplepets.brainsynder.nms.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityBlaze}
 */
@Size(width = 0.6f, length = 1.7f)
public class EntityBlazePet extends EntityPet implements IEntityBlazePet {
    private static final DataWatcherObject<Byte> ON_FIRE;

    static {
        ON_FIRE = DataWatcher.a(EntityBlazePet.class, DataWatcherWrapper.BYTE);
    }

    public EntityBlazePet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityBlazePet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(ON_FIRE, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("burning", isBurning());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("burning")) setBurning(object.getBoolean("burning"));
        super.applyCompound(object);
    }

    @Override
    public boolean isBurning() {
        return (this.datawatcher.get(ON_FIRE) & 1) != 0;
    }

    @Override
    public void setBurning(boolean var1) {
        byte b1 = this.datawatcher.get(ON_FIRE);
        if (var1) {
            b1 = (byte) (b1 | 1);
        } else {
            b1 &= -2;
        }

        this.datawatcher.set(ON_FIRE, b1);
    }
}
