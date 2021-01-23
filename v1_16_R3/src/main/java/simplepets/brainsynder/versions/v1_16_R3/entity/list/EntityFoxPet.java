package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.FoxType;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

import java.util.Optional;
import java.util.UUID;

public class EntityFoxPet extends EntityAgeablePet implements IEntityFoxPet {
    private static final DataWatcherObject<Integer> TYPE;
    private static final DataWatcherObject<Byte> FOX_FLAGS;
    private static final DataWatcherObject<Optional<UUID>> OWNER;
    private static final DataWatcherObject<Optional<UUID>> OTHER_TRUSTED;

    public EntityFoxPet(PetType type, PetUser user) {
        super(EntityTypes.FOX, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("type", getFoxType().name());
        compound.setBoolean("rolling-head", isRollingHead());
        compound.setBoolean("crouching", isCrouching());
        compound.setBoolean("sitting", isSitting());
        compound.setBoolean("sleeping", isPetSleeping());
        compound.setBoolean("angry", isAggressive());
        compound.setBoolean("walking", isWalking());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type")) setFoxType(FoxType.getByName(object.getString("type")));
        if (object.hasKey("rolling-head")) setRollingHead(object.getBoolean("rolling-head"));
        if (object.hasKey("crouching")) setCrouching(object.getBoolean("crouching"));
        if (object.hasKey("sitting")) setSitting(object.getBoolean("sitting"));
        if (object.hasKey("sleeping")) setPetSleeping(object.getBoolean("sleeping"));
        if (object.hasKey("angry")) setAggressive(object.getBoolean("angry"));
        if (object.hasKey("walking")) setWalking(object.getBoolean("walking"));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(OWNER, Optional.empty());
        datawatcher.register(OTHER_TRUSTED, Optional.empty());
        datawatcher.register(TYPE, FoxType.RED.ordinal());
        datawatcher.register(FOX_FLAGS, (byte)0);
    }

    @Override
    public FoxType getFoxType() {
        return FoxType.getByID(datawatcher.get(TYPE));
    }

    @Override
    public void setFoxType(FoxType type) {
        datawatcher.set(TYPE, type.ordinal());
    }

    @Override
    public boolean isPetSleeping() {
        return getSpecialFlag(32);
    }

    @Override
    public void setPetSleeping(boolean sleeping) {
        setSpecialFlag(32, sleeping);
    }

    @Override
    public void setSpecialFlag(int flag, boolean value) {
        if (value) {
            this.datawatcher.set(FOX_FLAGS, (byte)(this.datawatcher.get(FOX_FLAGS) | flag));
        } else {
            this.datawatcher.set(FOX_FLAGS, (byte)(this.datawatcher.get(FOX_FLAGS) & ~flag));
        }
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (datawatcher.get(FOX_FLAGS) & flag) != 0x0;
    }

    static {
        TYPE = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.INT);
        FOX_FLAGS = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.BYTE);
        OWNER = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.UUID);
        OTHER_TRUSTED = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.UUID);
    }
}
