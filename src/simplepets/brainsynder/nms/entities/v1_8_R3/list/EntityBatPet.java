package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityBatPet;
import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityBatPet extends EntityPet implements IEntityBatPet, IFlyablePet {
    public EntityBatPet(World world, IPet pet) {
        super(world, pet);
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    public EntityBatPet(World world) {
        super(world);
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
    public void t_() {
        super.t_();
        if (isHanging()) {
            this.motX = (this.motY = this.motZ = 0.0D);
            this.locY = (MathHelper.floor(this.locY) + 1.0D - this.length);
        } else {
            this.motY *= 0.6000000238418579D;
        }
    }

    @Override
    protected String getIdleSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    public boolean isHanging() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    @Override
    public void setHanging(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, (byte) (b0 | 1));
        } else {
            this.datawatcher.watch(16, (byte) (b0 & -2));
        }
    }

}
