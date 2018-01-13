package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityOcelotPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityTameablePet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityOcelotPet extends EntityTameablePet implements IEntityOcelotPet {
    public EntityOcelotPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityOcelotPet(World world) {
        super(world);
    }


    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("CatType", getCatType());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("CatType")) {
            setCatType(object.getInteger("CatType"));
        }
        super.applyCompound(object);
    }

    public int getCatType() {
        return this.datawatcher.getByte(18);
    }

    public void setCatType(int i) {
        this.datawatcher.watch(18, (byte) i);
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(18, (byte) 0);
    }
}
