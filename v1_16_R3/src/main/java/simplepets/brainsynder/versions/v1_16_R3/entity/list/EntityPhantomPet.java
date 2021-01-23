package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityPhantomPet extends EntityPet implements IEntitySlimePet {
    private static final DataWatcherObject<Integer> SIZE;

    public EntityPhantomPet(PetType type, PetUser user) {
        super(EntityTypes.PHANTOM, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SIZE, 1);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("size", getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("size")) setSize(object.getInteger("size"));
        super.applyCompound(object);
    }

    public int getSize() {
        return this.datawatcher.get(SIZE);
    }

    public void setSize(int i) {
        this.datawatcher.set(SIZE, i);
    }

    static {
        SIZE = DataWatcher.a(EntityPhantomPet.class, DataWatcherWrapper.INT);
    }
}
