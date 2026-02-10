package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.gameevent.GameEvent;
import simplepets.brainsynder.api.entity.passive.IEntityArmadilloPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ArmadilloPhase;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.armadillo.Armadillo}
 */
@SupportedVersion(version = ServerVersion.v1_20_5)
public class EntityArmadilloPet extends EntityAgeablePet implements IEntityArmadilloPet {
    private static final EntityDataAccessor<Armadillo.ArmadilloState> ARMADILLO_STATE = SynchedEntityData.defineId(EntityArmadilloPet.class, EntityDataSerializers.ARMADILLO_STATE);

    public EntityArmadilloPet(PetType type, PetUser user) {
        super(EntityType.ARMADILLO, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(ARMADILLO_STATE, Armadillo.ArmadilloState.IDLE);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("phase", getPhase().name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("phase", getPhase());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("phase")) setPhase(object.getEnum("phase", ArmadilloPhase.class));
        super.applyCompound(object);
    }

    @Override
    public ArmadilloPhase getPhase() {
        return ArmadilloPhase.getByName(entityData.get(ARMADILLO_STATE).name());
    }

    @Override
    public void setPhase(ArmadilloPhase phase) {
        this.gameEvent(GameEvent.ENTITY_ACTION);
        if (phase == ArmadilloPhase.STANDING) {
            this.makeSound(SoundEvents.ARMADILLO_UNROLL_FINISH);
        }else{
            this.makeSound(SoundEvents.ARMADILLO_ROLL);
        }

        entityData.set(ARMADILLO_STATE, Armadillo.ArmadilloState.valueOf(phase.getMojangName()));
    }
}
