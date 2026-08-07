package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.FoxVariant;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.util.Optional;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Fox.*;
import static simplepets.brainsynder.api.pet.PetDataRegistry.SLEEP;

/**
 * NMS: {@link net.minecraft.world.entity.animal.fox.Fox}
 */
public class EntityFoxPet extends EntityAgeablePet implements IEntityFoxPet {
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FOX_FLAGS = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OTHER_TRUSTED = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);


    public EntityFoxPet(PetType type, PetUser user) {
        super(EntitySelector.FOX, type, user);
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
        dataAccess.define(TYPE, FoxVariant.RED.ordinal());
        dataAccess.define(FOX_FLAGS, (byte)0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString(PetDataRegistry.Fox.TYPE.namespace(), getFoxType().name());
        compound.setBoolean(INTEREST.namespace(), isInterested());
        compound.setBoolean(CROUCHING.namespace(), isCrouching());
        compound.setBoolean(SITTING.namespace(), isSitting());
        compound.setBoolean(SLEEP.namespace(), isPetSleeping());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.Fox.TYPE.namespace())) setFoxType(object.getEnum(PetDataRegistry.Fox.TYPE.namespace(), FoxVariant.class, FoxVariant.RED));
        if (object.hasKey(INTEREST.namespace())) setInterested(object.getBoolean(INTEREST.namespace()));
        if (object.hasKey(CROUCHING.namespace())) setCrouching(object.getBoolean(CROUCHING.namespace()));
        if (object.hasKey(SITTING.namespace())) setSitting(object.getBoolean(SITTING.namespace()));
        if (object.hasKey(SLEEP.namespace())) setPetSleeping(object.getBoolean(SLEEP.namespace()));
        super.applyCompound(object);
    }

    @Override
    public FoxVariant getFoxType() {
        return FoxVariant.getByID(entityData.get(TYPE));
    }

    @Override
    public void setFoxType(FoxVariant type) {
        entityData.set(TYPE, type.ordinal());
    }

    @Override
    public boolean isCrouching() {
        return getSpecialFlag(4);
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
