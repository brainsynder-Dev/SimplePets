package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.FoxType;

import java.util.Optional;
import java.util.UUID;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityFox}
 */
@Size(width = 0.9F, length = 1.3F)
public class EntityFoxPet extends AgeableEntityPet implements IEntityFoxPet {
    private static final DataWatcherObject<Integer> TYPE;
    private static final DataWatcherObject<Byte> FOX_FLAGS;
    private static final DataWatcherObject<Optional<UUID>> OWNER;
    private static final DataWatcherObject<Optional<UUID>> OTHER_TRUSTED;

    static {
        TYPE = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.INT);
        FOX_FLAGS = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.BYTE);
        OWNER = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.UUID);
        OTHER_TRUSTED = DataWatcher.a(EntityFoxPet.class, DataWatcherWrapper.UUID);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(OWNER, Optional.empty());
        datawatcher.register(OTHER_TRUSTED, Optional.empty());
        datawatcher.register(TYPE, FoxType.RED.ordinal());
        datawatcher.register(FOX_FLAGS, (byte)0);
    }

    public EntityFoxPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityFoxPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("type", getFoxType().name());
        compound.setBoolean("rolling-head", isRollingHead());
        compound.setBoolean("crouching", isCrouching());
        compound.setBoolean("sitting", isSitting());
        compound.setBoolean("sleeping", isSleeping());
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
        if (object.hasKey("sleeping")) setSleeping(object.getBoolean("sleeping"));
        if (object.hasKey("angry")) setAggressive(object.getBoolean("angry"));
        if (object.hasKey("walking")) setWalking(object.getBoolean("walking"));
        super.applyCompound(object);
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
    public boolean getFlag(int i) {
        return (datawatcher.get(FOX_FLAGS) & i) != 0x0;
    }

    @Override
    public void setFlag(int i, boolean flag) {
        if (flag) {
            this.datawatcher.set(FOX_FLAGS, (byte)(this.datawatcher.get(FOX_FLAGS) | i));
        } else {
            this.datawatcher.set(FOX_FLAGS, (byte)(this.datawatcher.get(FOX_FLAGS) & ~i));
        }
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }


    @Override
    public void setRollingHead(boolean value){
        if (value && isSleeping()) setSleeping(false);
        setFlag(8, value);
    }
    @Override
    public void setCrouching(boolean value){
        setFlag(4, value);
    }
    @Override
    public void setSitting(boolean value){
        setFlag(1, value);
    }
    @Override
    public void setSleeping(boolean value){
        if (value && isWalking()) setWalking(false);
        if (value && isSitting()) setSitting(false);
        if (value && isCrouching()) setCrouching(false);
        setFlag(32, value);
    }
    @Override
    public void setAggressive(boolean value){
        setFlag(128, value);
    }
    @Override
    public void setWalking(boolean value){
        setFlag(64, value);
    }

    @Override
    public boolean isRollingHead () {
        return getFlag(8);
    }
    @Override
    public boolean isCrouching () {
        return getFlag(4);
    }
    @Override
    public boolean isSitting () {
        return getFlag(1);
    }
    @Override
    public boolean isSleeping () {
        return getFlag(32);
    }
    @Override
    public boolean isAggressive () {
        return getFlag(128);
    }
    @Override
    public boolean isWalking () {
        return getFlag(64);
    }
}
