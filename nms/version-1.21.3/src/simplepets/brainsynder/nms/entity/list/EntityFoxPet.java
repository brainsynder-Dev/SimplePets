package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.FoxType;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.util.Optional;
import java.util.UUID;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Fox}
 */
public class EntityFoxPet extends EntityAgeablePet implements IEntityFoxPet {
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FOX_FLAGS = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> OTHER_TRUSTED = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_UUID);


    public EntityFoxPet(PetType type, PetUser user) {
        super(EntityType.FOX, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("type", getFoxType().name());
        data.add("interested", isInterested());
        data.add("crouching", isCrouching());
        data.add("sitting", isSitting());
        data.add("sleep", isPetSleeping());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(OWNER, Optional.empty());
        dataAccess.define(OTHER_TRUSTED, Optional.empty());
        dataAccess.define(TYPE, FoxType.RED.ordinal());
        dataAccess.define(FOX_FLAGS, (byte)0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("type", getFoxType().name());
        compound.setBoolean("interested", isInterested());
        compound.setBoolean("crouching", isCrouching());
        compound.setBoolean("sitting", isSitting());
        compound.setBoolean("sleep", isPetSleeping());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type")) setFoxType(object.getEnum("type", FoxType.class, FoxType.RED));
        if (object.hasKey("interested")) setInterested(object.getBoolean("interested"));
        if (object.hasKey("crouching")) setCrouching(object.getBoolean("crouching"));
        if (object.hasKey("sitting")) setSitting(object.getBoolean("sitting"));
        if (object.hasKey("sleep")) setPetSleeping(object.getBoolean("sleep"));
        super.applyCompound(object);
    }

    @Override
    public FoxType getFoxType() {
        return FoxType.getByID(entityData.get(TYPE));
    }

    @Override
    public void setFoxType(FoxType type) {
        entityData.set(TYPE, type.ordinal());
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
            this.entityData.set(FOX_FLAGS, (byte)(this.entityData.get(FOX_FLAGS) | flag));
        } else {
            this.entityData.set(FOX_FLAGS, (byte)(this.entityData.get(FOX_FLAGS) & ~flag));
        }
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (entityData.get(FOX_FLAGS) & flag) != 0x0;
    }
}