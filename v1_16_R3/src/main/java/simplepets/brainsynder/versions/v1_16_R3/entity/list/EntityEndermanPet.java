package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.IBlockData;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

import java.util.Optional;

public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet {
    private static final DataWatcherObject<Optional<IBlockData>> CARRIED_BLOCK;
    private static final DataWatcherObject<Boolean> SCREAMING;

    public EntityEndermanPet(PetType type, PetUser user) {
        super(EntityTypes.ENDERMAN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(CARRIED_BLOCK, Optional.empty());
        this.datawatcher.register(SCREAMING, false);
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
        return this.datawatcher.get(SCREAMING);
    }

    @Override
    public void setScreaming(boolean flag) {
        this.datawatcher.set(SCREAMING, flag);
    }


    static {
        CARRIED_BLOCK = DataWatcher.a(EntityEndermanPet.class, DataWatcherWrapper.BLOCK);
        SCREAMING = DataWatcher.a(EntityEndermanPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
