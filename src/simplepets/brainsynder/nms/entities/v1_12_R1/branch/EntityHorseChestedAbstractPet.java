package simplepets.brainsynder.nms.entities.v1_12_R1.branch;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.main.IChestedAbstractPet;
import simplepets.brainsynder.pet.IPet;

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
        object.setBoolean("chested", isChested());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);
        if (object.hasKey("chested")) {
            setChested(object.getBoolean("chested"));
        }
    }

    @Override
    public boolean isChested() {
        return datawatcher.get(CHEST);
    }

    public void setChested(boolean flag) {
        this.datawatcher.set(CHEST, flag);
    }
}