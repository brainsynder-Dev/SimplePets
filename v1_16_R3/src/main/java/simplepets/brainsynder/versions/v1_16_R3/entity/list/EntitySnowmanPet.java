package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntitySnowmanPet extends EntityPet implements IEntitySnowmanPet {
    private static final DataWatcherObject<Byte> PUMPKIN;

    public EntitySnowmanPet(PetType type, PetUser user) {
        super(EntityTypes.SNOW_GOLEM, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(PUMPKIN, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("pumpkin", hasPumpkin());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("pumpkin")) setHasPumpkin(object.getBoolean("pumpkin"));
        super.applyCompound(object);
    }

    @Override
    public boolean hasPumpkin() {
        return (this.datawatcher.get(PUMPKIN) & 16) != 0;
    }

    @Override
    public void setHasPumpkin(boolean flag) {
        byte b0 = this.datawatcher.get(PUMPKIN);
        if (flag) {
            this.datawatcher.set(PUMPKIN, (byte) (b0 | 16));
        } else {
            this.datawatcher.set(PUMPKIN, (byte) (b0 & -17));
        }
    }

    static {
        PUMPKIN = DataWatcher.a(EntitySnowmanPet.class, DataWatcherWrapper.BYTE);
    }
}
