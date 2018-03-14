package simplepets.brainsynder.nms.entities.v1_12_R1;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityControllerPet;
import simplepets.brainsynder.pet.IPet;

public abstract class AgeableEntityPet extends EntityPet implements IAgeablePet {
    private static final DataWatcherObject<Boolean> BABY;

    static {
        BABY = DataWatcher.a(AgeableEntityPet.class, DataWatcherRegistry.h);
    }

    public AgeableEntityPet(World world) {
        super(world);
    }

    public AgeableEntityPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(BABY, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!(this instanceof IEntityControllerPet))
            object.setBoolean("baby", isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);
        if (!(this instanceof IEntityControllerPet))
            if (object.hasKey("baby")) setBaby(object.getBoolean("baby"));
    }

    public boolean isBaby() {
        return this.datawatcher.get(BABY);
    }

    public void setBaby(boolean flag) {
        this.datawatcher.set(BABY, flag);
    }
}
