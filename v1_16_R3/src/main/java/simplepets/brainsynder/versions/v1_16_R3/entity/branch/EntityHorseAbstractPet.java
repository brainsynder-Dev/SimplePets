package simplepets.brainsynder.versions.v1_16_R3.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

import java.util.Optional;
import java.util.UUID;

public class EntityHorseAbstractPet extends EntityAgeablePet implements IHorseAbstract {
    private static final DataWatcherObject<Byte> STATUS;
    private static final DataWatcherObject<Optional<UUID>> OWNER_UNIQUE_ID;

    public EntityHorseAbstractPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte b0 = this.datawatcher.get(STATUS);
        if(value){
            this.datawatcher.set(STATUS, (byte) (b0 | flag));
        }else{
            this.datawatcher.set(STATUS, (byte) (b0 & (~flag)));
        }
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (this.datawatcher.get(STATUS) & flag) != 0;
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", isSaddled());
        object.setBoolean("eating", isEating());
        object.setBoolean("angry", isAngry());
        object.setBoolean("rearing", isRearing());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("saddled")) setSaddled(object.getBoolean("saddled"));
        if (object.hasKey("eating")) setEating(object.getBoolean("eating"));
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry"));
        if (object.hasKey("rearing")) setRearing(object.getBoolean("rearing"));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STATUS, (byte) 0);
        this.datawatcher.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    static {
        STATUS = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherWrapper.BYTE);
        OWNER_UNIQUE_ID = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherWrapper.UUID);
    }
}