package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.misc.IFlyablePet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.EntityPet;
import simplepets.brainsynder.nms.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityBat}
 */
@Size(length = 0.9F)
public class EntityBatPet extends EntityPet implements IEntityBatPet,
        IFlyablePet {
    private static final DataWatcherObject<Byte> HANGING;

    static {
        HANGING = DataWatcher.a(EntityBatPet.class, DataWatcherWrapper.BYTE);
    }

    public EntityBatPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityBatPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(HANGING, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("hanging", isHanging());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("hanging")) setHanging(object.getBoolean("hanging"));
        super.applyCompound(object);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
/*
        if (this.isHanging()) {
            this.motX = this.motY = this.motZ = 0.0D;
            this.locY = (double) MathHelper.floor(this.locY) + 1.0D - (double) this.length;
        } else {
            this.motY *= 0.6000004238418579D;
        }
*/
    }

    @Override
    public boolean isHanging() {
        return (this.datawatcher.get(HANGING) & 1) != 0;
    }

    @Override
    public void setHanging(boolean flag) {
        byte var2 = this.datawatcher.get(HANGING);
        if (flag) {
            this.datawatcher.set(HANGING, (byte) (var2 | 1));
        } else {
            this.datawatcher.set(HANGING, (byte) (var2 & -2));
        }
    }
}
