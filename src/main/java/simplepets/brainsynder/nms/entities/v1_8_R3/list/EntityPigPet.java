package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityPigPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityPigPet extends AgeableEntityPet implements IEntityPigPet {
    public EntityPigPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityPigPet(World world) {
        super(world);
    }


    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Saddled", hasSaddle());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Saddled"))
            setSaddled(object.getBoolean("Saddled"));
        super.applyCompound(object);
    }

    @Override
    public boolean hasSaddle() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    @Override
    public void setSaddled(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, (byte) 1);
        } else {
            this.datawatcher.watch(16, (byte) 0);
        }
    }
}
