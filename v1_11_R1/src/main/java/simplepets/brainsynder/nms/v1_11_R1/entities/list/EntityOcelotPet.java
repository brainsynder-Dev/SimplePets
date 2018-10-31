package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.EntityTameablePet;

public class EntityOcelotPet extends EntityTameablePet implements IEntityOcelotPet {
    private static final DataWatcherObject<Integer> TYPE;

    static {
        TYPE = DataWatcher.a(EntityOcelotPet.class, DataWatcherRegistry.b);
    }

    public EntityOcelotPet(World world) {
        super(world);
    }

    public EntityOcelotPet(World world, IPet pet) {
        super(world, pet);
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
        return this.datawatcher.get(TYPE);
    }

    public void setCatType(int i) {
        this.datawatcher.set(TYPE, i);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TYPE, 0);
    }
}
