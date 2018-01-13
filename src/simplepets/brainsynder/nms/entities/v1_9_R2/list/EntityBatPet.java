package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.IFlyablePet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityBatPet extends EntityPet implements IEntityBatPet,
        IFlyablePet {
    private static final DataWatcherObject<Byte> byteWatcher;

    static {
        byteWatcher = DataWatcher.a(EntityBatPet.class, DataWatcherRegistry.a);
    }

    public EntityBatPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("hanging", isHanging());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("hanging")) {
            setHanging(object.getBoolean("hanging"));
        }
        super.applyCompound(object);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(byteWatcher, (byte) 0);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (this.isHanging()) {
            this.motX = this.motY = this.motZ = 0.0D;
            this.locY = (double) MathHelper.floor(this.locY) + 1.0D - (double) this.length;
        } else {
            this.motY *= 0.6000004238418579D;
        }
    }

    @Override
    public boolean isHanging() {
        return (this.datawatcher.get(byteWatcher) & 1) != 0;
    }

    @Override
    public void setHanging(boolean flag) {
        byte var2 = this.datawatcher.get(byteWatcher);
        if (flag) {
            this.datawatcher.set(byteWatcher, (byte) (var2 | 1));
        } else {
            this.datawatcher.set(byteWatcher, (byte) (var2 & -2));
        }
    }
}
