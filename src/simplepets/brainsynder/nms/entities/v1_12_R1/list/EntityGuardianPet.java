package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityGuardianPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet {
    private static final DataWatcherObject<Boolean> bz;
    private static final DataWatcherObject<Integer> bA;

    public EntityGuardianPet(World world) {
        super(world);
    }
    public EntityGuardianPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(bz, Boolean.FALSE);
        this.datawatcher.register(bA, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("elder", isElder());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("elder")) setElder(object.getBoolean("elder"));
        super.applyCompound(object);
    }

    static {
        bz = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.h);
        bA = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.b);
    }
}
