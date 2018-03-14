package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityEndermanPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet {
    public EntityEndermanPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityEndermanPet(World world) {
        super(world);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Short((short) 0));
        this.datawatcher.a(17, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("screaming", isScreaming());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) {
            setScreaming(object.getBoolean("screaming"));
        }
        super.applyCompound(object);
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
    public boolean isScreaming() {
        return this.datawatcher.getByte(18) > 0;
    }

    @Override
    public void setScreaming(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }
}
