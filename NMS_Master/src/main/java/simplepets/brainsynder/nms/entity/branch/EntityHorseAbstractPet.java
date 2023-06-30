package simplepets.brainsynder.nms.entity.branch;

import lib.brainsynder.ServerVersion;
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

import java.util.Optional;
import java.util.UUID;

public class EntityHorseAbstractPet extends EntityAgeablePet implements IHorseAbstract {
    private static final EntityDataAccessor<Byte> STATUS;
    private static EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID;

    protected boolean isJumping;

    public EntityHorseAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        doIndirectAttach = true;
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
        this.entityData.define(STATUS, (byte) 0);
        if (!ServerVersion.isNewer(ServerVersion.v1_19_4)) this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    static {
        STATUS = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.BYTE);
        if (!ServerVersion.isNewer(ServerVersion.v1_19_4)) OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.OPTIONAL_UUID);
    }
}
