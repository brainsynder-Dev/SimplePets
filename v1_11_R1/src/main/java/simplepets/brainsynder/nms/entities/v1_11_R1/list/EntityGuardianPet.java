package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.hostile.IEntityGuardianPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.EntityPet;

public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet {
    private static final DataWatcherObject<Boolean> bz;
    private static final DataWatcherObject<Integer> bA;

    static {
        bz = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.h);
        bA = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.b);
    }

    public EntityGuardianPet(World world) {
        super(world);
    }

    public EntityGuardianPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Elder", isElder());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Elder"))
            setElder(object.getBoolean("Elder"));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(bz, Boolean.FALSE);
        this.datawatcher.register(bA, 0);
    }

    @Override
    public boolean isElder() {
        return false;
    }

    @Override
    public void setElder(boolean var1) {

    }
}
