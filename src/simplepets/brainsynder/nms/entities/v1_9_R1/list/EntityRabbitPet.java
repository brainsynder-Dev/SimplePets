package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityRabbitPet;
import simplepets.brainsynder.nms.entities.v1_9_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.RabbitType;

@Deprecated
public class EntityRabbitPet extends AgeableEntityPet implements IEntityRabbitPet {
    private static final DataWatcherObject<Integer> TYPE;

    static {
        TYPE = DataWatcher.a(EntityRabbitPet.class, DataWatcherRegistry.b);
    }

    public EntityRabbitPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(TYPE, Integer.valueOf(0));
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("RabbitColor"))
            setRabbitType(RabbitType.values()[object.getInteger("RabbitColor")]);
        super.applyCompound(object);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("RabbitColor", getRabbitType().ordinal());
        return object;
    }

    public RabbitType getRabbitType() {
        return RabbitType.getByID(this.datawatcher.get(TYPE));
    }

    public void setRabbitType(RabbitType type) {
        this.datawatcher.set(TYPE, Integer.valueOf(type.getId()));
    }
}
