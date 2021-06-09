package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityBat}
 */
public class EntityBatPet extends EntityPet implements IEntityBatPet {
    private static final DataWatcherObject<Byte> HANGING;

    public EntityBatPet(PetType type, PetUser user) {
        super(EntityTypes.BAT, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(HANGING, (byte) 0);
    }

    @Override
    public boolean isHanging() {
        return (this.datawatcher.get(HANGING) & 1) != 0;
    }

    @Override
    public void setHanging(boolean flag) {
        byte var2 = this.datawatcher.get(HANGING);
        if (flag) {
            this.datawatcher.set(HANGING, (byte) (var2 | 1));
        } else {
            this.datawatcher.set(HANGING, (byte) (var2 & -2));
        }
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("hanging", isHanging());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("hanging")) setHanging(object.getBoolean("hanging"));
        super.applyCompound(object);
    }


    static {
        HANGING = DataWatcher.a(EntityBatPet.class, DataWatcherWrapper.BYTE);
    }
}
