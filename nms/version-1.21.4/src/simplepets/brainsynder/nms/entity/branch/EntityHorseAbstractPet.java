package simplepets.brainsynder.nms.entity.branch;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.util.Optional;
import java.util.UUID;

public class EntityHorseAbstractPet extends EntityAgeablePet implements IHorseAbstract {
    private static final EntityDataAccessor<Byte> STATUS = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.BYTE);
    private static EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID;

    static {
        if (!ServerVersion.isNewer(ServerVersion.v1_19_4)) OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.OPTIONAL_UUID);
    }

    protected boolean isJumping;

    public EntityHorseAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        doIndirectAttach = true;
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("saddled", isPetSaddled());
        data.add("eating", isEating());
        data.add("angry", isAngry());
        data.add("rearing", isRearing());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(STATUS, (byte) 0);
        if (!ServerVersion.isNewer(ServerVersion.v1_19_4)) dataAccess.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean flag) {
        this.isJumping = flag;
    }

    public double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }


    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte b0 = this.entityData.get(STATUS);
        if(value){
            this.entityData.set(STATUS, (byte) (b0 | flag));
        }else{
            this.entityData.set(STATUS, (byte) (b0 & (~flag)));
        }
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (this.entityData.get(STATUS) & flag) != 0;
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", isPetSaddled());
        object.setBoolean("eating", isEating());
        object.setBoolean("angry", isAngry());
        object.setBoolean("rearing", isRearing());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("saddled")) setPetSaddled(object.getBoolean("saddled"));
        if (object.hasKey("eating")) setEating(object.getBoolean("eating"));
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry"));
        if (object.hasKey("rearing")) setRearing(object.getBoolean("rearing"));
        super.applyCompound(object);
    }
}