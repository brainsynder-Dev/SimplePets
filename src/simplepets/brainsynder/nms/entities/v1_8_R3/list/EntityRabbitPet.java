package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityRabbitPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.RabbitColor;

@Deprecated
public class EntityRabbitPet extends AgeableEntityPet implements IEntityRabbitPet {
    public EntityRabbitPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityRabbitPet(World world) {
        super(world);
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(18, (byte) 0);
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("RabbitColor"))
            setRabbitType(RabbitColor.values()[object.getInteger("RabbitColor")]);
        super.applyCompound(object);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("RabbitColor", getRabbitType().ordinal());
        return object;
    }

    public RabbitColor getRabbitType() {
        return RabbitColor.getByID(this.datawatcher.getByte(18));
    }

    public void setRabbitType(RabbitColor type) {
        this.datawatcher.watch(18, Byte.valueOf((byte) type.getId()));
    }
}
