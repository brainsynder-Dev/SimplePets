package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import simplepets.brainsynder.api.entity.passive.IEntitySnifferPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.SnifferState;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

// TODO: Implement a state reset task, it will reset the state after a bit and reset it to do the animations
/**
 * NMS: {@link net.minecraft.world.entity.animal.sniffer.Sniffer}
 */
public class EntitySnifferPet extends EntityAgeablePet implements IEntitySnifferPet {
    private static final EntityDataAccessor<Sniffer.State> DATA_STATE = SynchedEntityData.defineId(EntitySnifferPet.class, EntityDataSerializers.SNIFFER_STATE);
    private static final EntityDataAccessor<Integer> DATA_DROP_SEED_AT_TICK = SynchedEntityData.defineId(EntitySnifferPet.class, EntityDataSerializers.INT);
    private SnifferState state = SnifferState.IDLING;

    public EntitySnifferPet(PetType type, PetUser user) {
        super(EntityType.SNIFFER, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("state", getSnifferState().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_STATE, Sniffer.State.IDLING);
        dataAccess.define(DATA_DROP_SEED_AT_TICK, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("state", getSnifferState());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("state")) setSnifferState(object.getEnum("state", SnifferState.class, SnifferState.IDLING));
        super.applyCompound(object);
    }

    @Override
    public SnifferState getSnifferState() {
        return state;
    }

    @Override
    public void setSnifferState(SnifferState state) {
        this.state = state;
        transitionTo(state);
    }

    private void setState(SnifferState state) {
        Sniffer.State sniffer_state = Sniffer.State.valueOf(state.name());
        this.entityData.set(DATA_STATE, sniffer_state);
    }

    //  ---- Implemented methods directly from the NMS Code ---- //
    public void transitionTo(SnifferState state) {
        switch (state) {
            case IDLING -> setState(SnifferState.IDLING);
            case FEELING_HAPPY -> {
                // targetTick = (tickCount + addition);
                playSound(SoundEvents.SNIFFER_HAPPY, 1.0F, 1.0F);
                setState(SnifferState.FEELING_HAPPY);
            }
            case SCENTING -> {
                setState(SnifferState.SCENTING);
                playSound(SoundEvents.SNIFFER_SCENTING, 1.0F, this.isBaby() ? 1.3F : 1.0F);
            }
            case SNIFFING -> {
                playSound(SoundEvents.SNIFFER_SNIFFING, 1.0F, 1.0F);
                setState(SnifferState.SNIFFING);
            }
            case DIGGING -> {
                setState(SnifferState.DIGGING);
                entityData.set(DATA_DROP_SEED_AT_TICK, tickCount + 120);
                VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)63);
            }
        }
    }
}
