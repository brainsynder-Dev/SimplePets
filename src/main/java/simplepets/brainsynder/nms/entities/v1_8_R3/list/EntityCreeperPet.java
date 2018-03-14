package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityCreeperPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.EntityPet;
import simplepets.brainsynder.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet {
    public EntityCreeperPet(World world, IPet pet) {
        super(world, pet);
        this.datawatcher.a(17, (byte) 0);
    }

    public EntityCreeperPet(World world) {
        super(world);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("powered", isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("powered")) {
            setPowered(object.getBoolean("powered"));
        }
        super.applyCompound(object);
    }

    @Override
    public boolean isPowered() {
        return this.datawatcher.getByte(17) == 1;
    }

    public void setPowered(boolean flag) {
        this.datawatcher.watch(17, (byte) (flag ? 1 : 0));
    }

    @Override
    protected String getIdleSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }
}
