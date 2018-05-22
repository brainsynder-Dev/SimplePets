package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityTameablePet;

@Size(width = 0.6F, length = 0.8F)
public class EntityOcelotPet extends EntityTameablePet implements IEntityOcelotPet {
    private static final DataWatcherObject<Integer> OCELOT_VARIANT;

    static {
        OCELOT_VARIANT = DataWatcher.a(EntityOcelotPet.class, DataWatcherRegistry.b);
    }

    public EntityOcelotPet(World world) {
        super(world);
    }
    public EntityOcelotPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(OCELOT_VARIANT, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("type", getCatType());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type")) setCatType(object.getInteger("type"));
        super.applyCompound(object);
    }

    public int getCatType() {
        return this.datawatcher.get(OCELOT_VARIANT);
    }

    public void setCatType(int i) {
        this.datawatcher.set(OCELOT_VARIANT, i);
    }
}
