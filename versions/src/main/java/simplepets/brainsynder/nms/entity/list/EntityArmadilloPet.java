package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import simplepets.brainsynder.api.entity.passive.IEntityArmadilloPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ArmadilloPhase;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Armadillo.PHASE;


/**
 * NMS: {@link net.minecraft.world.entity.animal.armadillo.Armadillo}
 */
@VersionLimit(min = {1, 20, 5})
public class EntityArmadilloPet extends EntityAgeablePet implements IEntityArmadilloPet {
    private static final EntityDataAccessor<Armadillo.ArmadilloState> ARMADILLO_STATE = SynchedEntityData.defineId(EntityArmadilloPet.class, EntityDataSerializers.ARMADILLO_STATE);

    public EntityArmadilloPet(PetType type, PetUser user) {
        super(EntitySelector.ARMADILLO, type, user);
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
        object.setEnum(PHASE.namespace(), getPhase());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PHASE.namespace())) setPhase(object.getEnum(PHASE.namespace(), ArmadilloPhase.class));
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
