package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGhast}
 */
public class EntityGhastPet extends EntityPet implements IEntityGhastPet {
    private static final DataWatcherObject<Boolean> ATTACKING;

    public EntityGhastPet(PetType type, PetUser user) {
        super(EntityTypes.GHAST, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(ATTACKING, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("screaming", isScreaming());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("screaming")) setScreaming(object.getBoolean("screaming"));
        super.applyCompound(object);
    }

    @Override
    public boolean isScreaming() {
        return this.datawatcher.get(ATTACKING);
    }

    @Override
    public void setScreaming(boolean flag) {
        this.datawatcher.set(ATTACKING, flag);
    }


    static {
        ATTACKING = DataWatcher.a(EntityGhastPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
