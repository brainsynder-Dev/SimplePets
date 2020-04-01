package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.AgeableEntityPet;

public class EntityPolarBearPet extends AgeableEntityPet implements IEntityPolarBearPet {
    private static final DataWatcherObject<Boolean> STANDING_UP;

    static {
        STANDING_UP = DataWatcher.a(EntityPolarBearPet.class, DataWatcherRegistry.h);
    }


    public EntityPolarBearPet(World world) {
        super(world);
    }

    public EntityPolarBearPet(World world, IPet pet) {
        super(world, pet);
    }


    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Standing", isStanding());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Standing"))
            setStandingUp(object.getBoolean("Standing"));
        super.applyCompound(object);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STANDING_UP, Boolean.FALSE);
    }

    public void setStandingUp(boolean flag) {
        this.datawatcher.set(STANDING_UP, flag);
    }

    @Override
    public boolean isStanding() {
        return this.datawatcher.get(STANDING_UP);
    }
}
