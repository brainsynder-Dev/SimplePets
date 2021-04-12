package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityVex}
 */
public class EntityVexPet extends EntityPet implements IEntityVexPet {
    protected static final DataWatcherObject<Byte> VEX_FLAGS;

    public EntityVexPet(PetType type, PetUser user) {
        super(EntityTypes.VEX, type, user);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.getDataWatcher().register(VEX_FLAGS, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("powered", isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("powered")) setPowered(object.getBoolean("powered"));
        super.applyCompound(object);
    }

    @Override
    public boolean isPowered() {
        return (datawatcher.get(VEX_FLAGS) & 1) != 0;
    }

    @Override
    public void setPowered(boolean value) {
        byte flag = this.datawatcher.get(VEX_FLAGS);
        int j;
        if (value) {
            j = flag | 1;
        } else {
            j = flag & ~1;
        }

        this.datawatcher.set(VEX_FLAGS, (byte) (j & 255));
    }

    static {
        VEX_FLAGS = DataWatcher.a(EntityVexPet.class, DataWatcherWrapper.BYTE);
    }
}
