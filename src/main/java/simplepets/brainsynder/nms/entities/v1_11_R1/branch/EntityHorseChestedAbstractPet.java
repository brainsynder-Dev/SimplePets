package simplepets.brainsynder.nms.entities.v1_11_R1.branch;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.IChestedAbstractPet;
import simplepets.brainsynder.api.pet.IPet;

public abstract class EntityHorseChestedAbstractPet extends EntityHorseAbstractPet implements IChestedAbstractPet {
    private static final DataWatcherObject<Boolean> CHEST;

    static {
        CHEST = DataWatcher.a(EntityHorseChestedAbstractPet.class, DataWatcherRegistry.h);
    }


    public EntityHorseChestedAbstractPet(World world) {
        super(world);
    }

    public EntityHorseChestedAbstractPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CHEST, Boolean.FALSE);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Chested", isChested());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Chested")) {
            setChested(object.getBoolean("Chested"));
        }
        super.applyCompound(object);
    }

    @Override
    public boolean isChested() {
        return datawatcher.get(CHEST);
    }

    public void setChested(boolean flag) {
        this.datawatcher.set(CHEST, flag);
    }
}